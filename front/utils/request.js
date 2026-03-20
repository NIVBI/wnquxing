// request.js
// 基于原生 fetch 封装的 HTTP 请求工具

/**
 * 默认配置
 */
const defaultConfig = {
    baseURL: '',                 // 基础路径，如：'https://api.example.com'
    timeout: 10000,               // 超时时间（毫秒）
    headers: {
        'Content-Type': 'application/json',
    },
};

/**
 * 将对象转换为 URL 查询字符串
 * @param {Object} params - 参数对象
 * @returns {string} 查询字符串（不含 ?）
 */
function stringifyParams(params) {
    if (!params) return '';
    return Object.keys(params)
        .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
        .join('&');
}

/**
 * 合并配置（深度合并）
 * @param {...Object} configs - 多个配置对象
 * @returns {Object} 合并后的配置
 */
function mergeConfig(...configs) {
    return configs.reduce((merged, current) => {
        if (!current) return merged;
        return {
            ...merged,
            ...current,
            headers: { ...merged.headers, ...current.headers },
        };
    }, {});
}

/**
 * 核心请求函数
 * @param {string} url - 请求路径（相对或绝对）
 * @param {Object} options - 请求选项
 * @param {string} options.method - 请求方法（GET、POST 等）
 * @param {Object} options.params - URL 查询参数
 * @param {any} options.data - 请求体数据（对象会被自动 JSON 序列化）
 * @param {Object} options.headers - 请求头
 * @param {number} options.timeout - 超时时间
 * @returns {Promise<any>} 返回解析后的响应数据
 */
async function request(url, options = {}) {
    // 合并配置
    const config = mergeConfig(defaultConfig, options);
    const { baseURL, timeout, headers, params, data, method = 'GET' } = config;

    // 处理 URL 前缀和查询参数
    let fullUrl = url.startsWith('http') ? url : baseURL + url;
    if (params) {
        const queryString = stringifyParams(params);
        if (queryString) {
            fullUrl += (fullUrl.includes('?') ? '&' : '?') + queryString;
        }
    }

    // 准备 fetch 的配置
    const fetchOptions = {
        method: method.toUpperCase(),
        headers: { ...headers },
    };

    // 处理请求体（如果方法允许携带 body）
    const methodWithBody = ['POST', 'PUT', 'PATCH'];
    if (data && methodWithBody.includes(fetchOptions.method)) {
        if (headers['Content-Type'] === 'application/json') {
            fetchOptions.body = JSON.stringify(data);
        } else {
            fetchOptions.body = data; // 其他类型直接传递（如 FormData）
        }
    }

    // 超时处理（使用 AbortController）
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), timeout);
    fetchOptions.signal = controller.signal;

    try {
        const response = await fetch(fullUrl, fetchOptions);
        clearTimeout(timeoutId);

        // 解析响应体（根据 Content-Type 自动选择）
        let responseData;
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            responseData = await response.json();
        } else {
            responseData = await response.text();
        }

        // 如果 HTTP 状态码不在 200-299 范围内，当作错误处理
        if (!response.ok) {
            const error = new Error(response.statusText || 'Request failed');
            error.status = response.status;
            error.data = responseData;
            throw error;
        }

        return responseData;
    } catch (error) {
        clearTimeout(timeoutId);
        // 统一错误处理：可以在这里加入错误日志或提示
        if (error.name === 'AbortError') {
            throw new Error(`请求超时（${timeout}ms）`);
        }
        throw error;
    }
}

/**
 * 封装常用 HTTP 方法
 */
export default {
    /**
     * GET 请求
     * @param {string} url - 请求路径
     * @param {Object} params - URL 查询参数
     * @param {Object} config - 其他配置（headers, timeout 等）
     * @returns {Promise}
     */
    get(url, params = {}, config = {}) {
        return request(url, { method: 'GET', params, ...config });
    },

    /**
     * POST 请求
     * @param {string} url - 请求路径
     * @param {Object|FormData} data - 请求体数据
     * @param {Object} config - 其他配置（headers, timeout 等）
     * @returns {Promise}
     */
    post(url, data = {}, config = {}) {
        return request(url, { method: 'POST', data, ...config });
    },

    /**
     * PUT 请求
     * @param {string} url - 请求路径
     * @param {Object|FormData} data - 请求体数据
     * @param {Object} config - 其他配置
     * @returns {Promise}
     */
    put(url, data = {}, config = {}) {
        return request(url, { method: 'PUT', data, ...config });
    },

    /**
     * DELETE 请求
     * @param {string} url - 请求路径
     * @param {Object} params - URL 查询参数
     * @param {Object} config - 其他配置
     * @returns {Promise}
     */
    delete(url, params = {}, config = {}) {
        return request(url, { method: 'DELETE', params, ...config });
    },

    /**
     * 文件上传（快捷方法）
     * @param {string} url - 请求路径
     * @param {File} file - 要上传的文件
     * @param {string} fileName - 表单字段名（默认 'file'）
     * @param {Object} config - 其他配置
     * @returns {Promise}
     */
    upload(url, file, fileName = 'file', config = {}) {
        const formData = new FormData();
        formData.append(fileName, file);
        return request(url, {
            method: 'POST',
            data: formData,
            headers: {}, // 让浏览器自动设置 Content-Type（multipart/form-data，包含 boundary）
            ...config,
        });
    },

    /**
     * 更新默认配置（如 baseURL, headers 等）
     * @param {Object} newConfig - 新的默认配置
     */
    setDefaultConfig(newConfig) {
        Object.assign(defaultConfig, newConfig);
    },
};
package com.wnquxing.utils;

public class TableSplitUtils {
  private static final String SPLIT_TABLE_MEETING_CHAT_MESSAGE = "meeting_chat_message";

  private static final String CREATE_TABLE_TEMP = "CREATE TABLE IF NOT EXISTS %s like %s;";

  private static final Integer SPLIT_TABLE_COUNT = 32;

  public static String getCreateTableSql(String templateTableName, Integer tableIndex, Integer tableCount) {
    Integer padLen = String.valueOf(tableCount).length();
    String tableName = templateTableName + "_" + String.format("%0" + padLen + "d", tableIndex);
    return String.format(CREATE_TABLE_TEMP, tableName, templateTableName);
  }

  private static void getSplitTableSQL() {
    for (int i = 1; i <= SPLIT_TABLE_COUNT; i++) {
      System.out.println(getCreateTableSql(SPLIT_TABLE_MEETING_CHAT_MESSAGE, i, SPLIT_TABLE_COUNT));
    }
  }

  public static void main(String[] args) {
    getSplitTableSQL();
  }

  private static String getTableName(String prefix, Integer tableCount, String key) {
    int hashCode = Math.abs(key.hashCode());
    Integer tableIndex = hashCode % SPLIT_TABLE_COUNT;
    Integer indexLen = String.valueOf(tableIndex).length();
    return prefix + "_" + String.format("%0" + indexLen + "d", tableIndex);
  }

  public static String getMeetingChatMessageTable(String meetingId) {
    return getTableName(SPLIT_TABLE_MEETING_CHAT_MESSAGE, SPLIT_TABLE_COUNT, meetingId);
  }
}

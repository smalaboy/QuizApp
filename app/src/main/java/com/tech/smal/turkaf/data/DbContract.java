package com.tech.smal.turkaf.data;

/**
 * Created by smoct on 23/02/2019.
 */

public final class DbContract {
    public static final String DB_NAME = "quiz.db";
    public static final int DB_VERSION = 2;

    public static final class QUESTIONS {
        public static final String TABLE_NAME = "questions";
        public static final String COL_ID = "id";
        public static final String COL_QUESTION = "question";
        public static final String COL_OPT_A = "opta";
        public static final String COL_OPT_B = "optb";
        public static final String COL_OPT_C = "optc";
        public static final String COL_OPT_D = "optd";
        public static final String COL_ANSWER = "answer";
        public static final String COL_USER_ID = "user_id";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_QUESTION + " VARCHAR(255), "
                + COL_OPT_A + " VARCHAR(255), "
                + COL_OPT_B + " VARCHAR(255), "
                + COL_OPT_C + " VARCHAR(255), "
                + COL_OPT_D + " VARCHAR(255), "
                + COL_ANSWER+ " VARCHAR(255), "
                + COL_USER_ID+ " INTEGER "
                + ");";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }


    public static final class USER {
        public static final String TABLE_NAME = "user";
        public static final String COL_ID = "id";
        public static final String COL_USERNAME = "username";
        public static final String COL_EMAIL = "email";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USERNAME + " VARCHAR(50), "
                + COL_EMAIL+ " VARCHAR(50) "
                + ");";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }


    public static final class QUESTION_GRADES {
        public static final String TABLE_NAME = "question_grades";
        public static final String COL_QUESTION_ID = "question_id";
        public static final String COL_USER_ID = "user_id";
        public static final String COL_GRADE = "grade";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_QUESTION_ID + " INTEGER, "
                + COL_USER_ID + " INTEGER, "
                + COL_GRADE+ " INTEGER "
                + ");";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
}






































































































































//package com.tech.smal.turkaf.data;

/**
 * Created by smoct on 23/02/2019.
 */

//public final class DbContract {
//    public static final String DB_NAME = "quiz.db";
//    public static final int DB_VERSION = 1;
//
//    public static final class QUESTIONS {
//        public static final String TABLE_NAME = "questions";
//        public static final String COL_ID = "id";
//        public static final String COL_QUESTION = "question";
//        public static final String COL_OPT_A = "opta";
//        public static final String COL_OPT_B = "optb";
//        public static final String COL_OPT_C = "optc";
//        public static final String COL_OPT_D = "optd";
//        public static final String COL_ANSWER = "answer";
//
//        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COL_QUESTION + " VARCHAR(255), "
//                + COL_OPT_A + " VARCHAR(255), "
//                + COL_OPT_B + " VARCHAR(255), "
//                + COL_OPT_C + " VARCHAR(255), "
//                + COL_OPT_D + " VARCHAR(255), "
//                + COL_ANSWER+ " VARCHAR(255) "
//                + ");";
//
//        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
//    }
//}

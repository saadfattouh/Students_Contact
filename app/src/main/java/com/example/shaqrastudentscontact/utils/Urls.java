package com.example.shaqrastudentscontact.utils;

public class Urls {

    //MAIN REQUESTS
    public static final String BASE_URL = "http://std.scit.co/student-contract/public/api/";

    public static final String REGISTER_STUDENT_URL = BASE_URL + "register-student";
    public static final String REGISTER_PROFESSOR_URL = BASE_URL + "register-professor";
    public static final String LOGIN_URL = BASE_URL + "login";
    public static final String RESET_PASSWORD_STUDENT_URL = BASE_URL + "edit_password_student";
    public static final String RESET_PASSWORD_PROFESSOR_URL = BASE_URL + "edit_password_professor";

    //STUDENT

    public static final String GET_CHAT_LIST = "";
    public static final String GET_MESSAGES_LIST = "";
    public static final String GET_DEPARTMENTS = BASE_URL +  "get_departments";
    public static final String GET_HONOR_STUDENTS = BASE_URL +  "";
    public static final String GET_PROFESSORS = BASE_URL +  "";
    public static final String GET_BOOKS = BASE_URL +  "get_books";
    public static final String GET_QUESTIONS_TO_PROF = BASE_URL +  "";
    public static final String SEND_QUESTION_TO_PROF = BASE_URL +  "";
    public static final String POST_COMMUNITY_QUESTION = BASE_URL +  "post_community_question";
    public static final String GET_COMMUNITY_QUESTIONS = BASE_URL +  "get_community_questions";
    public static final String GET_QUESTION_REPLIES = BASE_URL +  "get_community_question_replies";
    public static final String POST_REPLY_FOR_QUESTION = BASE_URL +  "post_community_reply";


    //PROFESSOR
    public static final String PROFESSOR_SCHEDULE = BASE_URL + "";

    public static final String EMAIL_VERIFICATION = BASE_URL +  "";
}

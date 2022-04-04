package com.example.shaqrastudentscontact;

public interface StudentContactTables {

    /**student
     *
     * int id
     * String name
     * string email
     * String password
     * int type //honor or normal
     */

    /**professor
     *
     * int id
     * String name
     * String email
     * String password
     * int department_id //foreign key
     * String specialization
     * String start_free_time
     * String end_free_time
     */

    /**community_questions
     *
     * int id
     * int student_id//foreign key
     * String question
     * int department_id //foreign key
     * date created_at
     * boolean is_common
     */
    /**community_replies
     *
     * int id
     * int student_id // foreign key
     * int question_id // foreign_key
     * String reply
     * date created_at
     */
    /**books
     *
     * int id
     * String title
     * String url
     */

    /**departments
     *
     * int id
     * String name
     */

    /**professor_questions
     * int id
     * int user_id // foreign key /student who asked this question
     * int professor_id // foreign key / professor  been asked
     * String title
     * String content
     * String answer
     * date created_at
     */

    /**honors_questions
     * int id
     * int user_id // foreign key /student who asked this question
     * int honor_id // foreign key / honor student  been asked
     * String title
     * String content
     * String answer
     * date created_at
     */
}

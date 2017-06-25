package com.example.myapplication;

import java.util.List;

public class ExamTopBean {
	public String tpo_name;

	public String md5;
	public List<Question>list;
	public class Question {
		public String id;
		public String tpo_id;
		public String question_number;
	}
}

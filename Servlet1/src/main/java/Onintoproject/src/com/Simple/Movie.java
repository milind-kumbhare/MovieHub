package com.Simple;

public class Movie {

	 private String tconst;
	    private String titleType;
	    private String primaryTitle;
	    private int runtimeMinutes;
	    private String genres;
		public String getTconst() {
			return tconst;
		}
		public void setTconst(String tconst) {
			this.tconst = tconst;
		}
		public String getTitleType() {
			return titleType;
		}
		public void setTitleType(String titleType) {
			this.titleType = titleType;
		}
		public String getPrimaryTitle() {
			return primaryTitle;
		}
		public void setPrimaryTitle(String primaryTitle) {
			this.primaryTitle = primaryTitle;
		}
		public int getRuntimeMinutes() {
			return runtimeMinutes;
		}
		public void setRuntimeMinutes(int runtimeMinutes) {
			this.runtimeMinutes = runtimeMinutes;
		}
		public String getGenres() {
			return genres;
		}
		public void setGenres(String genres) {
			this.genres = genres;
		}
		public Movie(String tconst, String titleType, String primaryTitle, int runtimeMinutes, String genres) {
			super();
			this.tconst = tconst;
			this.titleType = titleType;
			this.primaryTitle = primaryTitle;
			this.runtimeMinutes = runtimeMinutes;
			this.genres = genres;
		}

	}

package com.naveen.backingapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class StepsItem implements Parcelable{
	private String videoURL;
	private String description;
	private int id;
	private String shortDescription;
	private String thumbnailURL;

	protected StepsItem(Parcel in) {
		videoURL = in.readString();
		description = in.readString();
		id = in.readInt();
		shortDescription = in.readString();
		thumbnailURL = in.readString();
	}

	public static final Creator<StepsItem> CREATOR = new Creator<StepsItem>() {
		@Override
		public StepsItem createFromParcel(Parcel in) {
			return new StepsItem(in);
		}

		@Override
		public StepsItem[] newArray(int size) {
			return new StepsItem[size];
		}
	};

	public void setVideoURL(String videoURL){
		this.videoURL = videoURL;
	}

	public String getVideoURL(){
		return videoURL;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setShortDescription(String shortDescription){
		this.shortDescription = shortDescription;
	}

	public String getShortDescription(){
		return shortDescription;
	}

	public void setThumbnailURL(String thumbnailURL){
		this.thumbnailURL = thumbnailURL;
	}

	public String getThumbnailURL(){
		return thumbnailURL;
	}

	@Override
 	public String toString(){
		return 
			"StepsItem{" + 
			"videoURL = '" + videoURL + '\'' + 
			",description = '" + description + '\'' + 
			",id = '" + id + '\'' + 
			",shortDescription = '" + shortDescription + '\'' + 
			",thumbnailURL = '" + thumbnailURL + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(videoURL);
		dest.writeString(description);
		dest.writeInt(id);
		dest.writeString(shortDescription);
		dest.writeString(thumbnailURL);
	}
}

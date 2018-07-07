package com.naveen.backingapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipes implements Parcelable{
	private String image;
	private int servings;
	private String name;
	private List<IngredientsItem> ingredients;
	private int id;
	private List<StepsItem> steps;


	protected Recipes(Parcel in) {
		image = in.readString();
		servings = in.readInt();
		name = in.readString();
		ingredients = in.createTypedArrayList(IngredientsItem.CREATOR);
		id = in.readInt();
		steps = in.createTypedArrayList(StepsItem.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(image);
		dest.writeInt(servings);
		dest.writeString(name);
		dest.writeTypedList(ingredients);
		dest.writeInt(id);
		dest.writeTypedList(steps);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Recipes> CREATOR = new Creator<Recipes>() {
		@Override
		public Recipes createFromParcel(Parcel in) {
			return new Recipes(in);
		}

		@Override
		public Recipes[] newArray(int size) {
			return new Recipes[size];
		}
	};

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setServings(int servings){
		this.servings = servings;
	}

	public int getServings(){
		return servings;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setIngredients(List<IngredientsItem> ingredients){
		this.ingredients = ingredients;
	}

	public List<IngredientsItem> getIngredients(){
		return ingredients;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSteps(List<StepsItem> steps){
		this.steps = steps;
	}

	public List<StepsItem> getSteps(){
		return steps;
	}

	@Override
 	public String toString(){
		return 
			"Recipes{" + 
			"image = '" + image + '\'' + 
			",servings = '" + servings + '\'' + 
			",name = '" + name + '\'' + 
			",ingredients = '" + ingredients + '\'' + 
			",id = '" + id + '\'' + 
			",steps = '" + steps + '\'' + 
			"}";
		}



}
package com.mapzip.ppang.mapzipproject.map;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

public class RestaurantParsing {


    private RestaurantResult restaurants;
    private String response;
    private Context context;


    private boolean tagId_TITLE = false;
    private boolean tagId_LINK = false;
    private boolean tagId_CATEGORY = false;
    private boolean tagId_DESCRIPTION = false;
    private boolean tagId_TELEPHONE = false;
    private boolean tagId_ADRESS = false;
    private boolean tagId_ROADADRESS = false;
    private boolean tagId_KATECX = false;
    private boolean tagId_KATECY = false;


    private int katecX;//경도, X, longtitude
    private int katecY;//위도, Y, latitude

    private Restaurant restaurant = new Restaurant();

   private String SEOUL_ADRESS = "서울특별시";
    private boolean isSEOUL = true;



    public RestaurantParsing(RestaurantResult restaurants, String response, Context context) {

        this.restaurants = restaurants;
        this.response = response;
        this.context = context;
    }


    public boolean startParsing() {

        try {

            final XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();

            final XmlPullParser parser = parserCreator.newPullParser();

            StringReader stringReader = new StringReader(response);


            parser.setInput(stringReader);


            int parserEvent = parser.getEventType();

            String tag;

            while (parserEvent != XmlPullParser.END_DOCUMENT) {

                switch (parserEvent) {

                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.END_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:

                        tag = parser.getName();

                        if (tag.equals("title")) {
                            tagId_TITLE = true;
                        } else if (tag.equals("link")) {
                            tagId_LINK = true;
                        } else if (tag.equals("category")) {
                            tagId_CATEGORY = true;
                        } else if (tag.equals("description")) {
                            tagId_DESCRIPTION = true;
                        } else if (tag.equals("telephone")) {
                            tagId_TELEPHONE = true;
                        } else if (tag.equals("address")) {
                            tagId_ADRESS = true;
                        } else if (tag.equals("roadAddress")) {
                            tagId_ROADADRESS = true;
                        } else if (tag.equals("mapx")) {
                            tagId_KATECX = true;
                        } else if (tag.equals("mapy")) {
                            tagId_KATECY = true;
                        }
                        break;

                    case XmlPullParser.END_TAG:

                        setTagIdFalse();
                        break;

                    case XmlPullParser.TEXT:

                        if (tagId_TITLE == true) {

                            restaurant.setTitle(trimTitle(parser.getText()));
                                    setTagIdFalse();
                            //Log.d("volley", "0~>: " + parser.getText().trim());

                        } else if (tagId_LINK == true) {

                            restaurant.setLink(trimTitle(parser.getText()));
                            setTagIdFalse();
                            //Log.d("volley", "1: " + parser.getText().trim());

                        } else if (tagId_CATEGORY == true) {

                            restaurant.setCategory(trimTitle(parser.getText()));
                            setTagIdFalse();
                            //Log.d("volley", "2: " + parser.getText().trim());

                        } else if (tagId_DESCRIPTION == true) {

                            restaurant.setDescription(trimTitle(parser.getText()));
                            setTagIdFalse();
                            //Log.d("volley", "3: " + parser.getText().trim());

                        } else if (tagId_TELEPHONE == true) {

                            restaurant.setTelephone(trimTitle(parser.getText()));
                            setTagIdFalse();
                            //Log.d("volley", "4: " + parser.getText().trim());

                        } else if (tagId_ADRESS == true) {

                            if(parser.getText().contains(SEOUL_ADRESS)){

                                restaurant.setAdress(trimTitle(parser.getText()));
                                setTagIdFalse();
                                //Log.d("volley", "5: " + parser.getText().trim());
                            }else{

                                setTagIdFalse();
                                isSEOUL = false;
                                break;
                            }

                        } else if (tagId_ROADADRESS == true) {

                            restaurant.setRoadadress(trimTitle(parser.getText()));
                            setTagIdFalse();
                            //Log.d("volley", "6: " + parser.getText().trim());

                        } else if (tagId_KATECX == true) {

                            katecX = Integer.parseInt(trimTitle(parser.getText()));

                            restaurant.setKatecX(katecX);
                            setTagIdFalse();
                            //Log.d("volley", "7: " + parser.getText().trim());

                        } else if (tagId_KATECY == true) {

                            katecY = Integer.parseInt(trimTitle(parser.getText()));

                            restaurant.setKatecY(katecY);
                            setTagIdFalse();
                            //Log.d("volley", "8: " + parser.getText().trim());

                            //이음식점이 서울에 위치하는가?
                            if(isSEOUL) {

                                restaurant = addRestaurant(restaurant);
                            }
                            setIsSeoulTrue();
                        }

                        setTagIdFalse();
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return true;
    }

    public void setTagIdFalse() {

        tagId_TITLE = false;
        tagId_LINK = false;
        tagId_CATEGORY = false;
        tagId_DESCRIPTION = false;
        tagId_TELEPHONE = false;
        tagId_ADRESS = false;
        tagId_ROADADRESS = false;
        tagId_KATECX = false;
        tagId_KATECY = false;
    }

    // restaurants.add(restaurant);
    // restaurant = new Restaurant(); 이 두 부분을 메소드화 시킨 메소드
    public Restaurant addRestaurant(Restaurant restaurant) {

        restaurants.add(restaurant);
        return new Restaurant();
    }

    public RestaurantResult getRestaurants() {
        return restaurants;
    }



    public String trimTitle(String originalName){

        String returnString = originalName;

        returnString = returnString.trim();
        returnString = returnString.replaceAll("<b>", "");
        returnString = returnString.replaceAll("</b>", "");
        returnString = returnString.replaceAll("&amp;", "&");

//        returnString = returnString.replaceAll("&nbsp;"," ");
//        returnString = returnString.replaceAll("&quot;", "\"");
//        returnString = returnString.replaceAll("&lt;", "<");
//        returnString = returnString.replaceAll("&gt;", ">");

      return  returnString;
    }


    public void setIsSeoulTrue(){

        isSEOUL = true;
    }
}


package com.example.receveintent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsLinkDataValidation {
    public boolean checkEachArticleFormat(JSONArray articles) {
        boolean articlesValidationResult=true;
        for(int i=0;i<articles.length();i++)
        {
            try {
                JSONObject jsonObject=articles.getJSONObject(i);
               articlesValidationResult=validSingleArticles(jsonObject);


            } catch (JSONException e) {
                articlesValidationResult=false;
                e.printStackTrace();
            }
        }
      return  articlesValidationResult;
    }

    public boolean validSingleArticles(JSONObject jsonObject) {

        boolean articlesValidationResult=true;
        try {
        if(!isLinkUrlValid(jsonObject.getString("link")))
        {
            articlesValidationResult=false;
        }
        if(!isLinkUrlValid(jsonObject.getString("imageUrl")))
        {
            articlesValidationResult=false;

        }
        if(!(jsonObject.getString("title").length()>0))
        {
            articlesValidationResult=false;

        }
        if(!(jsonObject.getString("category").length()>0))
        {
            articlesValidationResult=false;
        }

            if(!(jsonObject.getLong("timestamp")>0))
            {
                articlesValidationResult=false;
            }
        } catch (JSONException e) {
            articlesValidationResult=false;
            e.printStackTrace();
        }
        return articlesValidationResult;
    }

    public boolean validateFirestoreDocumentWithMultipleArticles(JSONObject jsonObject) {
        try {
            if(jsonObject.getJSONArray("articles").length()>0)
            {
                return checkEachArticleFormat(jsonObject.getJSONArray("articles"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }



    private boolean isLinkUrlValid(String link) {
        return urlValidator(link);

    }
    private static final String URL_REGEX =
            "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
                    "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
                    "([).!';/?:,][[:blank:]])?$";

    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    public  boolean urlValidator(String url) {

        if (url == null) {
            return false;
        }

        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }
}

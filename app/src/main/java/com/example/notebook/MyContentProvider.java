package com.example.notebook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import org.litepal.LitePal;

public class MyContentProvider extends ContentProvider {
    public static final int WORD_DIR=0;
    public static final int WORD_ITEM=1;
    public static final String AUTHORITY="com.example.notebook.provider";
    private static UriMatcher uriMatcher;
    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"word",WORD_DIR);
        uriMatcher.addURI(AUTHORITY,"word/#",WORD_ITEM);
    }
    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int deleteRows=0;
        deleteRows=LitePal.deleteAll("delete from word where en = ?",selection);
        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        return "vnd.android.cursor,dir/vnd.com.example.notebook.provider.word";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        Uri uriReturn=null;
        word word=new word();
        word.setEn(values.getAsString("en"));
        word.setZh(values.getAsString("zh"));
        word.save();
        return null;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            case WORD_DIR:
                cursor= LitePal.findBySQL("select * from word where en = ? ",selection);
                break;
            case WORD_ITEM:
                String wordId=uri.getPathSegments().get(1);
                cursor=LitePal.findBySQL("select * from word where en = ? ",wordId);
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        word word=new word();
        word.setEn(values.getAsString("en"));
        word.setZh(values.getAsString("zh"));
        word.save();
        return 1;
    }
}

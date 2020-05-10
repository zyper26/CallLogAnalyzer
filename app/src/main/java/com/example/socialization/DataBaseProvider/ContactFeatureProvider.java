package com.example.socialization.DataBaseProvider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.example.socialization.DataBaseSQL.ContactFeatureDBAdapter;

public class ContactFeatureProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.socialization";

    public static final String PATH_CONTACT_FEATURE_LIST = "CONTACT_FEATURE_LIST";
//    public static final String PATH_TODO_PLACE ="TODO_LIST_FROM_PLACE";
    public static final String PATH_CONTACT_FEATURE_COUNT_ALL ="CONTACT_FEATURE_COUNT";

    public static final Uri CONTENT_URI_1 = Uri.parse("content://"+AUTHORITY+"/"+ PATH_CONTACT_FEATURE_LIST);
//    public static final Uri CONTENT_URI_2=Uri.parse("content://"+AUTHORITY+"/"+ PATH_TODO_PLACE);
    public static final Uri CONTENT_URI_3 = Uri.parse("content://"+AUTHORITY+"/"+ PATH_CONTACT_FEATURE_COUNT_ALL);

    public static final int CONTACT_FEATURE_LIST_ALL = 1;
//    public static final int TODOS_FROM_SPECIFIC_PLACE=2;
    public static final int CONTACT_FEATURE_COUNT = 3;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, PATH_CONTACT_FEATURE_LIST, CONTACT_FEATURE_LIST_ALL);
//        MATCHER.addURI(AUTHORITY, PATH_TODO_PLACE,TODOS_FROM_SPECIFIC_PLACE);
        MATCHER.addURI(AUTHORITY, PATH_CONTACT_FEATURE_COUNT_ALL, CONTACT_FEATURE_COUNT);
    }

    public static final String MIME_TYPE_1 = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+ "com.example.socialization.contactpredictionframework";
//    public static final String MIME_TYPE_2 = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+ "vnd.com.codetutore.todos.place";
    public static final String MIME_TYPE_3 = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+ "com.example.socialization.contactpredictionframework";



    private ContactFeatureDBAdapter contactFeatureDBAdapter;


    @Override
    public boolean onCreate() {
        contactFeatureDBAdapter = ContactFeatureDBAdapter.getContactFeatureDBAdapterInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection,String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (MATCHER.match(uri)){
            case CONTACT_FEATURE_LIST_ALL:
                cursor = contactFeatureDBAdapter.getCursorsForContactFeatures();
                break;
            case CONTACT_FEATURE_COUNT:
                cursor = contactFeatureDBAdapter.getCount();
                break;

//            case TODOS_FROM_SPECIFIC_PLACE: cursor=toDoListDBAdapter.getCursorForSpecificPlace(strings1[0]);break;
//            case TODOS_COUNT:cursor=toDoListDBAdapter.getCount();break;
            default:cursor = null; break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)){
            case CONTACT_FEATURE_LIST_ALL: return MIME_TYPE_1;
//            case TODOS_FROM_SPECIFIC_PLACE: return MIME_TYPE_2;
             case CONTACT_FEATURE_COUNT: return MIME_TYPE_3;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri,ContentValues values) {
        Uri returnUri = null;
        switch (MATCHER.match(uri)){
            case CONTACT_FEATURE_LIST_ALL:
                returnUri = insertContactFeatures(uri,values);
                break;
            default: new UnsupportedOperationException("insert operation not supported"); break;
        }

        return returnUri ;
    }


    private int deleteContactFeatures(String whereClause, String [] whereValues){
        return contactFeatureDBAdapter.delete(whereClause,whereValues);
    }


    private Uri insertContactFeatures(Uri uri, ContentValues values) {
        long id = contactFeatureDBAdapter.insert(values);
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse("content://"+AUTHORITY+"/"+ PATH_CONTACT_FEATURE_LIST +"/"+id);
    }

    private int updateContactFeatures(ContentValues contentValues, String whereCluase, String [] strings){
        return contactFeatureDBAdapter.update(contentValues,whereCluase,strings);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleteCount=-1;
        switch (MATCHER.match(uri)){
            case CONTACT_FEATURE_LIST_ALL: deleteCount = deleteContactFeatures(selection,selectionArgs);break;
            default:new UnsupportedOperationException("delete operation not supported"); break;
        }
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updateCount=-1;
        switch (MATCHER.match(uri)){
            case CONTACT_FEATURE_LIST_ALL: updateCount = updateContactFeatures(values,selection,selectionArgs);break;
            default:new UnsupportedOperationException("insert operation not supported"); break;
        }
        return updateCount;
    }
}

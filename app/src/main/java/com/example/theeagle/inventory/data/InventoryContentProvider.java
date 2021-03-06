package com.example.theeagle.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.theeagle.inventory.R;

public class InventoryContentProvider extends ContentProvider {
    private static final int PRODUCT = 100;
    private static final int PRODUCT_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private DatabaseHelper databaseHelper;

    static {
        sUriMatcher.addURI(Contract.Product.AUTHORITY, Contract.Product.INVENTORY_PATH, PRODUCT);
        sUriMatcher.addURI(Contract.Product.AUTHORITY, Contract.Product.INVENTORY_PATH + "/#", PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                cursor = database.query(Contract.Product.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = Contract.Product._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(Contract.Product.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(Resources.getSystem().getString(R.string.unkown_uri) + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return Contract.Product.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return Contract.Product.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException(Resources.getSystem().getString(R.string.unkown_uri)
                        + uri + Resources.getSystem().getString(R.string.with_match)
                        + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return insertNewItem(uri, values);

            default:
                throw new IllegalArgumentException(Resources.getSystem().getString(R.string.insertion_notsupported) + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(Contract.Product.TABLE_NAME, selection, selectionArgs);
            case PRODUCT_ID:
                selection = Contract.Product._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(Contract.Product.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(Resources.getSystem().getString(R.string.delete_notsupported) + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                if (values != null)
                    return updateProduct(uri, values, selection, selectionArgs);
            case PRODUCT_ID:
                selection = Contract.Product._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                if (values != null)
                    return updateProduct(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(Resources.getSystem().getString(R.string.insertion_notsupported) + uri);
        }
    }

    private Uri insertNewItem(Uri uri, ContentValues values) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        long id = database.insert(Contract.Product.TABLE_NAME, null, values);
        if (id == -1) {
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private int updateProduct(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues.containsKey(Contract.Product.PRODUCT_NAME)) {
            String productName = contentValues.getAsString(Contract.Product.PRODUCT_NAME);
            if (productName == null) {
                throw new IllegalArgumentException(Resources.getSystem().getString(R.string.update_not_supported) + uri);
            }
        }
        if (contentValues.containsKey(Contract.Product.PRICE)) {
            Integer price = contentValues.getAsInteger(Contract.Product.PRICE);
            if (price == null || price <= 0) {
                throw new IllegalArgumentException(Resources.getSystem().getString(R.string.update_not_supported) + uri);
            }
        }
        if (contentValues.containsKey(Contract.Product.QUANTITY)) {
            Integer quantity = contentValues.getAsInteger(Contract.Product.PRICE);
            if (quantity == null || quantity < 0) {
                throw new IllegalArgumentException(Resources.getSystem().getString(R.string.update_not_supported) + uri);
            }
        }
        if (contentValues.containsKey(Contract.Product.SUPPLIER_NAME)) {
            String supplierName = contentValues.getAsString(Contract.Product.PRODUCT_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException(Resources.getSystem().getString(R.string.update_not_supported) + uri);
            }
        }
        if (contentValues.containsKey(Contract.Product.SUPPLIER_PHONE_NUMBER)) {
            String supplierPhoneNumber = contentValues.getAsString(Contract.Product.PRODUCT_NAME);
            if (supplierPhoneNumber == null) {
                throw new IllegalArgumentException(Resources.getSystem().getString(R.string.update_not_supported) + uri);
            }
        }
        if (contentValues.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri, null);
        return database.update(Contract.Product.TABLE_NAME, contentValues, selection, selectionArgs);
    }

}

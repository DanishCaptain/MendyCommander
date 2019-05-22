package org.mendybot.commander.android.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.mendybot.commander.android.model.store.MediaDbHelper;

public abstract class MediaProvider extends ContentProvider {
    private final String tableName;
    private MediaDbHelper helper;

    public MediaProvider(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public final boolean onCreate() {
        helper = new MediaDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public final Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = helper.getReadableDatabase().query(
                tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    public final Cursor query() {
        Cursor cursor = helper.getReadableDatabase().query(
                tableName,
                null,
                null,
                null,
                null,
                null,
                getOrderBy());

//        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    protected abstract String getOrderBy();

    @Nullable
    @Override
    public final String getType(@NonNull Uri uri) {
        throw new RuntimeException("not implemented.");
    }

    @Nullable
    @Override
    public final Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        throw new RuntimeException("not implemented.");
    }

    @Override
    public final int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = helper.getWritableDatabase();

        db.beginTransaction();
        int rowsInserted = 0;
        try {
            for (ContentValues value : values) {
                if (value == null) {
                    continue;
                }
                //String uuid = value.getAsString(MovieTable.COLUMN_UUID);
                //String title = value.getAsString(MovieTable.COLUMN_TITLE);
                /*
                if (!NewsDateUtils.isDateNormalized(dateTime)) {
                    dateTime = NewsDateUtils.normalizeDate(dateTime);
                    value.put(ArticleTable.COLUMN_TIME, dateTime);
                }
                */

                long _id = db.insert(tableName, null, value);
                if (_id != -1) {
                    rowsInserted++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsInserted;
    }

    @Override
    public final int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        numRowsDeleted = helper.getWritableDatabase().delete(
                tableName,
                selection,
                selectionArgs);

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public final int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selections, @Nullable String[] selectionArgs) {
        throw new RuntimeException("not implemented.");
    }

    @Override
    public final void shutdown() {
        helper.close();
        super.shutdown();
    }
}

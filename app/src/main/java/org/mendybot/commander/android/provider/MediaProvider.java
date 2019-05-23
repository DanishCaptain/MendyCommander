package org.mendybot.commander.android.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.mendybot.commander.android.model.store.MediaDbHelper;
import org.mendybot.commander.android.model.store.MediaTable;

public final class MediaProvider extends ContentProvider {
    private MediaDbHelper helper;

    @Override
    public final boolean onCreate() {
        helper = new MediaDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public final Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = helper.getReadableDatabase().query(
                MediaTable.TABLE_NAME,
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
                MediaTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "order by "+ MediaTable.COLUMN_KEY+" DESC");

//        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


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

                long _id = db.insert(MediaTable.TABLE_NAME, null, value);
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
                MediaTable.TABLE_NAME,
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

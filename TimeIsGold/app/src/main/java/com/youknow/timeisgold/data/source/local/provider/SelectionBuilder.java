package com.youknow.timeisgold.data.source.local.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Aaron on 02/09/2017.
 */

public class SelectionBuilder {
    private String mTable = null;
    private HashMap<String, String> mProjectionMap;
    private StringBuilder mSelection;
    private ArrayList<String> mSelectionArgs;

    /**
     * Reset any internal state, allowing this builder to be recycled.
     */
    public SelectionBuilder reset() {
        mTable = null;
        if (mProjectionMap != null) {
            mProjectionMap.clear();
        }
        if (mSelection != null) {
            mSelection.setLength(0);
        }
        if (mSelectionArgs != null) {
            mSelectionArgs.clear();
        }
        return this;
    }

    /**
     * Append the given selection clause to the internal state. Each clause is
     * surrounded with parenthesis and combined using {@code AND}.
     */
    public SelectionBuilder where(String selection, String... selectionArgs) {
        if (TextUtils.isEmpty(selection)) {
            if (selectionArgs != null && selectionArgs.length > 0) {
                throw new IllegalArgumentException(
                        "Valid selection required when including arguments=");
            }

            // Shortcut when clause is empty
            return this;
        }

        ensureSelection(selection.length());
        if (mSelection.length() > 0) {
            mSelection.append(" AND ");
        }

        mSelection.append("(").append(selection).append(")");
        if (selectionArgs != null) {
            ensureSelectionArgs();
            for (String arg : selectionArgs) {
                mSelectionArgs.add(arg);
            }
        }

        return this;
    }

    public SelectionBuilder table(String table) {
        mTable = table;
        return this;
    }

    private void assertTable() {
        if (mTable == null) {
            throw new IllegalStateException("Table not specified");
        }
    }

    private void ensureProjectionMap() {
        if (mProjectionMap == null) {
            mProjectionMap = new HashMap<String, String>();
        }
    }

    private void ensureSelection(int lengthHint) {
        if (mSelection == null) {
            mSelection = new StringBuilder(lengthHint + 8);
        }
    }

    private void ensureSelectionArgs() {
        if (mSelectionArgs == null) {
            mSelectionArgs = new ArrayList<String>();
        }
    }

    public SelectionBuilder mapToTable(String column, String table) {
        ensureProjectionMap();
        mProjectionMap.put(column, table + "." + column);
        return this;
    }

    public SelectionBuilder map(String fromColumn, String toClause) {
        ensureProjectionMap();
        mProjectionMap.put(fromColumn, toClause + " AS " + fromColumn);
        return this;
    }

    /**
     * Return selection string for current internal state.
     *
     * @see #getSelectionArgs()
     */
    public String getSelection() {
        if (mSelection != null) {
            return mSelection.toString();
        } else {
            return null;
        }
    }

    /**
     * Return selection arguments for current internal state.
     *
     * @see #getSelection()
     */
    public String[] getSelectionArgs() {
        if (mSelectionArgs != null) {
            return mSelectionArgs.toArray(new String[mSelectionArgs.size()]);
        } else {
            return null;
        }
    }

    private void mapColumns(String[] columns) {
        if (mProjectionMap == null) return;
        for (int i = 0; i < columns.length; i++) {
            final String target = mProjectionMap.get(columns[i]);
            if (target != null) {
                columns[i] = target;
            }
        }
    }

    @Override
    public String toString() {
        return "SelectionBuilder[table=" + mTable + ", selection=" + getSelection()
                + ", selectionArgs=" + Arrays.toString(getSelectionArgs()) + "]";
    }

    /**
     * Execute query using the current internal state as {@code WHERE} clause.
     */
    public Cursor query(SQLiteDatabase db, String[] columns, String orderBy) {
        return query(db, columns, null, null, orderBy, null);
    }

    /**
     * Execute query using the current internal state as {@code WHERE} clause.
     */
    public Cursor query(SQLiteDatabase db, String[] columns, String groupBy,
                        String having, String orderBy, String limit) {
        assertTable();
        if (columns != null) mapColumns(columns);
        return db.query(mTable, columns, getSelection(), getSelectionArgs(), groupBy, having,
                orderBy, limit);
    }

    /**
     * Execute update using the current internal state as {@code WHERE} clause.
     */
    public int update(SQLiteDatabase db, ContentValues values) {
        assertTable();
        return db.update(mTable, values, getSelection(), getSelectionArgs());
    }

    /**
     * Execute delete using the current internal state as {@code WHERE} clause.
     */
    public int delete(SQLiteDatabase db) {
        assertTable();
        return db.delete(mTable, getSelection(), getSelectionArgs());
    }
}
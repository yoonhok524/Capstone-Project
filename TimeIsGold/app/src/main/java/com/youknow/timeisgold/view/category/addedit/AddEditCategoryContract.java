package com.youknow.timeisgold.view.category.addedit;

import com.youknow.timeisgold.data.Category;

/**
 * Created by Aaron on 03/09/2017.
 */

public interface AddEditCategoryContract {
    interface View {

        void finish();
    }

    interface Presenter {

        void setView(View view);

        void saveCategory(Category category);
    }
}

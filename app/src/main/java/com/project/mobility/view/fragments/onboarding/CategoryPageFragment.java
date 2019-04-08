package com.project.mobility.view.fragments.onboarding;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.mobility.R;
import com.project.mobility.view.fragments.onboarding.adapter.CategoryItemDetailLookup;
import com.project.mobility.view.fragments.onboarding.adapter.CategoryRecyclerViewAdapter;
import com.project.mobility.view.fragments.onboarding.keyprovider.CustomKeyProvider;
import com.project.mobility.viewmodel.onboarding.OnboardingCategoryViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class CategoryPageFragment extends Fragment implements FragmentFinishedListener {

    public static final int RECYCLERVIEW_COLUMN_COUNT = 2;
    private static final String CATEGORY_SELECTION_ID = "category_selection";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final int MINIMUM_CATEGORY_SELECTION = 2;

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.selected_categories_text) TextView selectedCategoriesText;
    @BindView(R.id.selected_categories_text2) TextView selectedCategoriesDummyText;

    private int mColumnCount = 1;
    private OnboardingCategoryViewModel onboardingCategoryViewModel;
    private CategoryRecyclerViewAdapter adapter;

    public CategoryPageFragment() {
    }

    @SuppressWarnings("unused")
    public static CategoryPageFragment newInstance() {
        CategoryPageFragment fragment = new CategoryPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, RECYCLERVIEW_COLUMN_COUNT);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        onboardingCategoryViewModel = ViewModelProviders.of(this).get(OnboardingCategoryViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        ButterKnife.bind(this, view);
        setSelectedCategoriesCountMessage(0);

        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        onboardingCategoryViewModel.getCategories().observe(this, categories -> {
            adapter = new CategoryRecyclerViewAdapter(categories);
            recyclerView.setAdapter(adapter);
            SelectionTracker<Long> selectionTracker = new SelectionTracker.Builder<>(
                    CATEGORY_SELECTION_ID,
                    recyclerView,
                    new CustomKeyProvider(recyclerView),
                    new CategoryItemDetailLookup(recyclerView),
                    StorageStrategy.createLongStorage())
                    .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                    .build();

            selectionTracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
                @Override
                public void onSelectionChanged() {
                    super.onSelectionChanged();
                    int selectionSize = selectionTracker.getSelection().size();
                    setSelectedCategoriesCountMessage(selectionSize);
                    if (selectionSize >= MINIMUM_CATEGORY_SELECTION) {
                        setSelectedCategoriesPosition(selectionTracker.getSelection());
                    }
                }
            });

            adapter.setSelectionTracker(selectionTracker);
        });

        return view;
    }

    @OnClick(R.id.shuffle_button)
    public void shuffleListItems() {
        adapter.shuffleList();
        subscribeNotificationsTopics();

    }

    private void setSelectedCategoriesPosition(Selection<Long> selectedCategories) {
        List<Integer> categoryPositions = new ArrayList<>();
        for (long category : selectedCategories) {
            categoryPositions.add((int) category);
        }

        adapter.setSelectedCategoriesPosition(categoryPositions);
    }

    private void setSelectedCategoriesCountMessage(int selectedCategoriesCount) {
        selectedCategoriesText.setText(getString(R.string.onboarding_selected_categories, selectedCategoriesCount));
    }

    @Override
    public void doFinish() {
        Timber.d("Fragment is sending finish");
        subscribeNotificationsTopics();
    }

    private void subscribeNotificationsTopics() {
        if (adapter.getSelectedCategoriesName() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String categoryName : adapter.getSelectedCategoriesName()) {
                stringBuilder.append(categoryName);
                stringBuilder.append(" ");
                Timber.d(categoryName);
            }
            selectedCategoriesDummyText.setText(stringBuilder);
        }
    }
}

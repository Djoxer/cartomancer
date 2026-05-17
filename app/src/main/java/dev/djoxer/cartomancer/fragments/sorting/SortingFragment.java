package dev.djoxer.cartomancer.fragments.sorting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import dev.djoxer.cartomancer.R;
import dev.djoxer.cartomancer.fragments.dialogs.SortingDialogFragment;
import dev.djoxer.cartomancer.util.tarot.Card;
import dev.djoxer.cartomancer.util.Cartomancer;

import java.util.Collections;
import java.util.List;


public class SortingFragment extends Fragment {
    private static final String ARG_RUNNING = "argRunning";

    private RecyclerView recyclerView;
    private List<Card> list;
    protected SortingRecViewAdapter adapter;

    public static SortingFragment newInstance(boolean start) {
        SortingFragment fragment = new SortingFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_RUNNING, start);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sorting, container, false);
        recyclerView = v.findViewById(R.id.rec_view_sorting);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        adapter = new SortingRecViewAdapter(getActivity());
        adapter.setParent(this);
        list = Cartomancer.buildTrumps().list();
        list.remove(0);
        list.remove(list.size() - 1);

        while (hasMatch()) Collections.shuffle(list);

        adapter.setList(list);

        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        if (!isRunning()) {
            SortingDialogFragment dialog = new SortingDialogFragment();
            dialog.show(getParentFragmentManager(), "SortingDialog");
        }

        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int from = viewHolder.getAdapterPosition();
            int to = target.getAdapterPosition();

            update(from, to);
            adapter.notifyItemMoved(from, to);
            finish();

            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    public void update(int from, int to) {
        Collections.swap(list, from, to);
    }

    public void finish() {
        if (isSorted()) {
            Bundle args = new Bundle();
            args.putBoolean(ARG_RUNNING, false);
            setArguments(args);

            Toast.makeText(getActivity(), R.string.text_sorted, Toast.LENGTH_LONG).show();
        }
    }

    protected boolean isSorted() {
        boolean ret = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).value != i + 1) ret = false;
        }
        return ret;
    }

    protected boolean hasMatch() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).value == i + 1) return true;
        }
        return false;
    }

    protected boolean isRunning() {
        return getArguments().getBoolean(ARG_RUNNING);
    }
}
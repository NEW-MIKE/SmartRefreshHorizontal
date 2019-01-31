package com.scwang.smartrefresh.horizontal.demo.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.horizontal.demo.R;
import com.scwang.smartrefresh.horizontal.demo.adapter.BaseRecyclerAdapter;
import com.scwang.smartrefresh.horizontal.demo.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Arrays;
import java.util.Collection;

import static android.R.layout.simple_list_item_2;

/**
 * A simple {@link Fragment} subclass.
 */
public class HorizontalExampleFragment extends Fragment {

    private QuickAdapterAdapter mQuickAdapter;
    private BaseRecyclerAdapter<Void> mAdapter;

    int[] colorIds = {
            android.R.color.holo_blue_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_red_dark,
            android.R.color.holo_orange_dark,
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_example_horizontal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        final Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        final RecyclerView recyclerView = root.findViewById(R.id.recyclerView);

        recyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<Void>(simple_list_item_2) {

            @Override
            protected void onBindViewHolder(SmartViewHolder holder, Void model, int position) {
                holder.itemView.getLayoutParams().width = -2;//DensityUtil.dp2px(100);
                holder.itemView.getLayoutParams().height = -1;
                holder.itemView.setBackgroundResource(colorIds[position%colorIds.length]);
                holder.text(android.R.id.text1, getString(R.string.item_example_number_title, position));
                holder.textColorId(android.R.id.text1, android.R.color.white);

            }
        });
        recyclerView.setAdapter(mQuickAdapter = new QuickAdapterAdapter());


        mAdapter.refresh(initData());
        mQuickAdapter.replaceData(initData());
        mQuickAdapter.openLoadAnimation(QuickAdapterAdapter.ALPHAIN);

        RefreshLayout refreshLayout = root.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(root.getContext()));
//        refreshLayout.setRefreshFooter(new RefreshFooterWrapper(new MaterialHeader(root.getContext())), -1, -2);
////        refreshLayout.setEnableHeaderTranslationContent(true);
//        refreshLayout.setEnableFooterTranslationContent(true);
//        refreshLayout.setEnableAutoLoadMore(false);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.refresh(initData());
                        mQuickAdapter.replaceData(initData());
                        refreshLayout.finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                recyclerView.stopScroll();
                recyclerView.stopNestedScroll();
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.loadMore(initData());
                        mQuickAdapter.addData(initData());
                        refreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
    }

    private Collection<Void> initData() {
        return Arrays.asList(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
    }

    public class QuickAdapterAdapter extends BaseQuickAdapter<Void, BaseViewHolder> {
        QuickAdapterAdapter() {
            super(simple_list_item_2);
        }

        @Override
        protected void convert(BaseViewHolder holder, Void item) {
            int position = holder.getAdapterPosition();
            holder.itemView.getLayoutParams().width = -2;//DensityUtil.dp2px(100);
            holder.itemView.getLayoutParams().height = -1;
            holder.itemView.setBackgroundResource(colorIds[position%colorIds.length]);
            holder.setText(android.R.id.text1, getString(R.string.item_example_number_title, position));
            holder.setTextColor(android.R.id.text1, 0xffffffff);
        }
    }
}

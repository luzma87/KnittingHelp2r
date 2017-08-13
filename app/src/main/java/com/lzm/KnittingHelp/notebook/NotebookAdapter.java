package com.lzm.KnittingHelp.notebook;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lzm.KnittingHelp.R;
import com.lzm.KnittingHelp.databinding.PatternItemBinding;
import com.lzm.KnittingHelp.model.Pattern;

import java.util.List;

public class NotebookAdapter extends RecyclerView.Adapter<NotebookAdapter.PatternViewHolder> {

    private List<? extends Pattern> patterns;

    @Nullable
    private final PatternClickCallback patternClickCallback;

    public NotebookAdapter(@Nullable PatternClickCallback clickCallback) {
        patternClickCallback = clickCallback;
    }

    public void setPatternList(final List<? extends Pattern> patternList) {
        if (patterns == null) {
            patterns = patternList;
            notifyItemRangeInserted(0, patternList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return patterns.size();
                }

                @Override
                public int getNewListSize() {
                    return patternList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return patterns.get(oldItemPosition).getId() ==
                            patternList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Pattern newPattern = patternList.get(newItemPosition);
                    Pattern oldPattern = patternList.get(oldItemPosition);

                    return newPattern.getId() == oldPattern.getId()
                            && newPattern.getName().equals(oldPattern.getName())
                            && newPattern.getContent().equals(oldPattern.getContent());
                }
            });
            patterns = patternList;
            result.dispatchUpdatesTo(this);
        }
    }


    @Override
    public PatternViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PatternItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.pattern_item,
                        parent, false);
        binding.setCallback(patternClickCallback);
        return new PatternViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PatternViewHolder holder, int position) {
        holder.binding.setPattern(patterns.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return patterns == null ? 0 : patterns.size();
    }


    static class PatternViewHolder extends RecyclerView.ViewHolder {
        final PatternItemBinding binding;

        public PatternViewHolder(PatternItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

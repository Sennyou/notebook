package com.example.notebook;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class LeftFragment extends Fragment {
    private EditText input;
    private boolean isTwoPane;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view=inflater.inflate(R.layout.left_fragment,container,false);
        input=view.findViewById(R.id.input_exitText);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input=editable.toString();
                List<word> wordList=new ArrayList<>();
                word word=new word();
                word.setEn("网上查询");
                wordList.add(word);
                wordList.addAll(LitePal.where("en like ?","%"+input+"%").find(word.class));
                Item item=new Item(wordList);
                RecyclerView wordRecyclerView=getView().findViewById(R.id.rec_view);
                LinearLayoutManager layoutManager=new LinearLayoutManager((getActivity()));
                wordRecyclerView.setLayoutManager(layoutManager);
                wordRecyclerView.setAdapter(item);
            }
        });
        RecyclerView wordRecyclerView=view.findViewById(R.id.rec_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager((getActivity()));
        wordRecyclerView.setLayoutManager(layoutManager);
        Item item=new Item(getwords());
        wordRecyclerView.setAdapter(item);
        return view;
    }
    public void refresh(){
        RecyclerView wordRecyclerView=getView().findViewById(R.id.rec_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager((getActivity()));
        wordRecyclerView.setLayoutManager(layoutManager);
        Item item=new Item(getwords());
        wordRecyclerView.setAdapter(item);
    }
    private List<word> getwords(){
        List<word> wordList= LitePal.order("en desc").find(word.class);
        return wordList;
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.right)!=null){
            isTwoPane=true;
        }
        else
            isTwoPane=false;
    }

    class Item extends RecyclerView.Adapter<Item.ViewHolder>{
        private List<word> wordList;
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView worden;
            public  ViewHolder(View view){
                super(view);
                worden=(TextView)view.findViewById(R.id.word_en);
            }
        }
        public Item(List<word> words){
            wordList=words;
        }
        public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item,parent,false);
            final ViewHolder holder=new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                          word word=wordList.get(holder.getAdapterPosition());
                          RightFragment rightFragment=(RightFragment)getFragmentManager().findFragmentById(R.id.right);
                          rightFragment.Destroy();
                          if(isTwoPane){
                              if(word.getEn()=="网上查询"){
                                  rightFragment.refresh(word.getEn(),input.getText().toString());
                              }
                              else {
                                  rightFragment.refresh(word.getEn());
                              }
                          }
                          else{
                              wordContent_fragment.actionStart(getActivity(),word.getEn());
                          }
                }
            });
            return  holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            word word=wordList.get(position);
            holder.worden.setText(word.getEn());
        }

        @Override
        public int getItemCount() {
            return wordList.size();
        }

    }
}

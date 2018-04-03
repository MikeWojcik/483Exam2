package rose.wojcikmg.exam2wojcikmg;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kingm on 3/17/2018.
 */

public class StateAdaptor extends RecyclerView.Adapter<StateAdaptor.ViewHolder> implements ItemTouchHelperAdaptor{

    private Context myContext;
    final ArrayList<Territory> mStates = new ArrayList<>();
    private ArrayList<Territory> unusedStates;
    private Random mRandom = new Random();
    private int score;
    private int currentCapitalSelection;





    public StateAdaptor(Context context){

        myContext = context;
        unusedStates = new ArrayList<>();
        currentCapitalSelection = -1;
        unusedStates.addAll(FileUtils.loadFromJsonArray(myContext));
        score = 0;

        for(int i =0; i < 5; i++){
           mStates.add(addState());
        }


    }

    @Override
    public int getItemCount() {
        return mStates.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Territory current = mStates.get(position);
        //customize name
        String name = current.name;
        TextView nameTextView = holder.stateName;
        nameTextView.setText(name);

    }

    public Territory addState(){
        //gets a random index
        int rand = mRandom.nextInt(unusedStates.size());

        //deletes from unused states so quiz can end after all states are done
        Territory temp = unusedStates.get(rand);
        unusedStates.remove(rand);

        return temp;
    }

    public void removeStateCapital(int pos){
        Snackbar.make(MainActivity.view,mStates.get(pos).capital, Snackbar.LENGTH_INDEFINITE ).show();
        currentCapitalSelection = pos;

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position, int direction) {
        if(position == currentCapitalSelection) {
            if (direction == ItemTouchHelper.LEFT) {
                score--;

            } else if (direction == ItemTouchHelper.RIGHT) {
                score += 2;
            }
            mStates.remove(position);
            notifyItemRemoved(position);
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchViewHolder{
        TextView stateName;


        public ViewHolder(View retView){
            super(retView);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    removeStateCapital(getAdapterPosition());
                }

            });

            stateName = retView.findViewById(R.id.stateTextView);



                    }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }

        }

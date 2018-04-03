package rose.wojcikmg.exam2wojcikmg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private StateAdaptor mStateAdaptor;
    public static RecyclerView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStateAdaptor = new StateAdaptor(this);


        view = (RecyclerView)findViewById(R.id.list_view);

        view.setAdapter(mStateAdaptor);
        view.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback= new SwipeItemTouchCallback(mStateAdaptor);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(view);

        Toolbar top= findViewById(R.id.toolbar);
       // top.se


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_settings:
                    return true;

            default:
                return true;
            }
        }

    }

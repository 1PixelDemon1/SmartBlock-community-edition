package com.vimers.smartblock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AppListActivity extends AppCompatActivity {

    protected static RecyclerView recyclerView;
    //Basic OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        setRecyclerView();
        setDivider();
    }
    //Sets menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_list_menu, menu);
        return true;
    }
    //Listener/handler of selected menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.appListMenuItemAll:
                BlockedAppsListManager.setAllPossible();
                setRecyclerView();
                return true;
            case R.id.appListMenuItemNone:
                BlockedAppsListManager.clearSet();
                setRecyclerView();
                return true;
            case R.id.appListMenuItemAuto:
                BlockedAppsListManager.setAllAuto();
                setRecyclerView();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //Sets or Re-sets recycler view on Activity
    private void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.appList);
        BlockedAppsListAdapter adapter = new BlockedAppsListAdapter(this, getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA));
        recyclerView.setAdapter(adapter);
    }
    //Sets divider to recycler view
    private void setDivider() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}

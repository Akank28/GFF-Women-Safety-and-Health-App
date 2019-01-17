package vit.codebuster.com.codebusters;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class home extends Fragment {


    Toolbar toolbar;
    DrawerLayout drawer;
    public home() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        drawer = (DrawerLayout) root.findViewById(R.id.drawer_layout);

        // Inflate the layout for this fragment
        return root;


    }

    private void displaySelectedScreen(int itemid)
    {
        Fragment fragment = null;



        if (itemid == 1) {

            toolbar.setTitle("GFF");
            fragment = new home();
            // Handle the camera action
        } else if (itemid == 2) {

            toolbar.setTitle("Health");
            fragment = new health();

        } else if (itemid == 3) {

            toolbar.setTitle("Period Tracker");
            fragment = new PeriodTracker();

        } else if (itemid == 4) {

            Intent intent = new Intent(getContext(),emergency.class);
            startActivity(intent);

        } else if (itemid == R.id.nav_setting) {
            Intent intent = new Intent(getContext(),Setting.class);
            startActivity(intent);

        } else {
            toolbar.setTitle("GGIF");
            fragment = new home();
        }
        if (fragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout, fragment);
            ft.commit();
        }


        drawer.closeDrawer(GravityCompat.START);

    }

}

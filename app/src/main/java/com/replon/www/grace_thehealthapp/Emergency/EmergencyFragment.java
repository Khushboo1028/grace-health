package com.replon.www.grace_thehealthapp.Emergency;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.VideoPlayerActivity;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyFragment extends Fragment {

    View view;

    ImageView search;

    RelativeLayout layout_emergency_numbers,layout_first_aid,layout_safety_tips,layout_natural_disaster;

    ArrayList<ContentsEmergencyData> dataList;



    public EmergencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), getActivity());
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_emergency, container, false);

        init();


//        ------------------- EMERGENCY NUMBERS -------------------       //

        dataList.add(new ContentsEmergencyData(
                "National Emergency Helpline",
                "0",
                "112",
                "national emergency,national,emergency,helpline,112",
                "",
                0));

        dataList.add(new ContentsEmergencyData("Police",
                "0",
                "100",
                "police,100",
                "",
                0));

        dataList.add(new ContentsEmergencyData(
                "Fire Brigade",
                "0",
                "101",
                "fire brigade,fire,brigade,101",
                "",
                0));

        dataList.add(new ContentsEmergencyData(
                "Ambulance",
                "0",
                "108",
                "ambulance,108",
                "",
                0));

        dataList.add(new ContentsEmergencyData(
                "Gas Leakage Helpline",
                "0",
                "1800229944",
                "gas leakage,gas,leakage,helpline,1800229944",
                "",
                0));

        dataList.add(new ContentsEmergencyData(
                "Railway Enquiry",
                "0",
                "139",
                "railway enquiry,railway,enquiry,139",
                "",
                0));

        dataList.add(new ContentsEmergencyData("Blood Bank Helpline",
                "0",
                "104",
                "blood bank,blood,bank,helpline,104",
                "",
                0));

        dataList.add(new ContentsEmergencyData(
                "Missing Child or Women Helpline",
                "0",
                "1094",
                "missing child,missing women,missing,child,women,helpline,woman,1094",
                "",
                0));

        dataList.add(new ContentsEmergencyData(
                "Children in Difficult Situations Helpline",
                "0",
                "1098",
                "children in difficult situations,children,difficult,situation,helpline,1098",
                "",
                0));

        dataList.add(new ContentsEmergencyData(
                "Senior Citizen Helpline",
                "0",
                "1291",
                "senior citizen,senior,citizen,helpline,1291",
                "",
                0));

        dataList.add(new ContentsEmergencyData(
                "Road Accident Emergency",
                "0",
                "1073",
                "road accident,road,accident,emergency,1073",
                "",
                0));

        dataList.add(new ContentsEmergencyData(
                "Disaster Management (NDMA)",
                "0",
                "1078",
                "disaster management,disaster,management,ndma,1078",
                "",
                0));

        dataList.add(new ContentsEmergencyData(
                "AIDS Helpline",
                "0",
                "1097",
                "aids helpline,aids,helpline,1097",
                "",
                0));


        //        ------------------- FIRST AID -------------------       //


        dataList.add(new ContentsEmergencyData(
                "Artificial Respiration",
                "1",
                "artificial_respiration",
                "artificial respiration,artificial,respiration,throat,breathe,chest,nose,airway,breathing,blowing,mouth,resuscitation,air,mouth to mouth,inhale,exhale,revive,passage,oxygen",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fartificial_respiration_cpr_drowning.mp4?alt=media&token=749ecb46-5c45-4ed9-8104-3b024a8a84aa",
                R.drawable.artificial_respiration));

        dataList.add(new ContentsEmergencyData(
                "Cardiopulmonary resuscitation (CPR)",
                "1",
                "cpr",
                "cardiopulmonary resuscitation,resuscitation,cardiopulmonary,cpr,breathe,chest,mouth,breathing,compressions,cardiac,gasping,exhaustion,inhale,exhale,heart",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fartificial_respiration_cpr_drowning.mp4?alt=media&token=749ecb46-5c45-4ed9-8104-3b024a8a84aa",
                R.drawable.cpr));

        dataList.add(new ContentsEmergencyData(
                "Shock",
                "1",
                "shock",
                "shock,oxygen,spinal cord injury,aggressive behaviour,yawning,gasping,heart failure,shallow breathing,nausea,bleeding,septic,fear,severe bleeding,fast pulse,grey blue skin,restlessness,unresponsive",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fshock.mp4?alt=media&token=318a5f2b-9056-4e4f-b9d5-53210c1f4e79",
                R.drawable.shock));

        dataList.add(new ContentsEmergencyData(
                "Heart Attack",
                "1",
                "heart_attack",
                "heart attack,heart,attack,uncomfortable,pressure,breath,light head,defibrillator,cpr,artificial respiration,respiration,chest,chest pain,sweating,nausea,short breath,fainting,abdomen",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fheart_attack.mp4?alt=media&token=dd187778-1102-4b88-adb7-66ad9b1843e9",
                R.drawable.heart_attack));

        dataList.add(new ContentsEmergencyData(
                "Suffocation or Asphyxia",
                "1",
                "suffocation",
                "suffocation,asphyxia,chest,choking,bluish,high pulse rate,dilated pupils,drowning,strangulation,asthma,throat infection,oxygen,unconsciousness",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fchoking_suffocation_hanging_throttling.mp4?alt=media&token=74cf51a7-40fc-4049-8088-a4572100e90a",
                R.drawable.suffocation));

        dataList.add(new ContentsEmergencyData(
                "Cases of swallowing tongue",
                "1",
                "swallowing_tongue",
                "cases of swallowing tongue,tongue,swallow,swallowing,palate,convolusion",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fswallowing_tongue.mp4?alt=media&token=73d00f2e-fa88-4135-bec5-740b7f4a3700",
                R.drawable.swallowing_tongue));

        dataList.add(new ContentsEmergencyData(
                "First Aid for Hanging, Strangling and Throttling",
                "1",
                "hanging_strangling_throttling",
                "hanging strangling throttling,hanging,strangling,throttling,neck,red spots,congestion,broken neck,unconsciousness,constriction,resuscitate",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fchoking_suffocation_hanging_throttling.mp4?alt=media&token=74cf51a7-40fc-4049-8088-a4572100e90a",
                R.drawable.hanging_strangling_throttling));

        dataList.add(new ContentsEmergencyData(
                "Drowning",
                "1",
                "drowning",
                "drowning,drowned,under water,water,sea,suicide,swimming,boating,pools,ice,thin ice,unconsciousness,bluish skin,coughing froth",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fartificial_respiration_cpr_drowning.mp4?alt=media&token=749ecb46-5c45-4ed9-8104-3b024a8a84aa",
                R.drawable.drowning));

        dataList.add(new ContentsEmergencyData(
                "Fainting",
                "1",
                "fainting",
                "fainting,unresponsive,unconsciousness,pain,exhaustion,hunger,low sugar,stress,emotional stress, slow pulse,cold skin,pale skin,sweating",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Ffainting.mp4?alt=media&token=066498ef-fe7f-4e80-a432-84d1d3f0289b",
                R.drawable.fainting));

        dataList.add(new ContentsEmergencyData(
                "Diabetic Emergency",
                "1",
                "diabetes",
                "diabetic emergency,diabetic,diabetes,insulin,blood sugar,sugar,glucose,hyperglycemic,hypoglycemic,unconsciousness,unresponsive,dry skin,rapid breathing,fast breathing,fast pulse,sweet breath,sweating,nervousness,chills,trembling",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fdiabetic_emergency.mp4?alt=media&token=2e72c9ef-456b-4d47-8f10-2f9ed615f2dc",
                R.drawable.diabetes));

        dataList.add(new ContentsEmergencyData(
                "Stroke",
                "1",
                "stroke",
                "stroke,fast,f.a.s.t,face,arms,speech,time,difficulty speaking,confusion,numbness,headache,vision loss,facial droop,ischemic,hemorrhagic,thrombotic,embolic,intracerebral,brain,subarachnoid,paralysis",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fstroke.mp4?alt=media&token=d1f554aa-a209-426b-a317-d3b646e334ce",
                R.drawable.stroke));

        dataList.add(new ContentsEmergencyData(
                "Diagnosed case of death",
                "1",
                "death",
                "diagnosed case of death,case of death,death,drugs,unconsciousness,unresponsive,fire,smoke,electric wire,toxic gas,no pulse,no breath",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fconfirmation_of_death.mp4?alt=media&token=5b2fc6c5-df96-4837-a825-d64bd46cb0f0",
                R.drawable.death));


        dataList.add(new ContentsEmergencyData(
                "Burns and scalds",
                "1",
                "burns_and_scalds",
                "burns and scalds,burns,scalds,heat,hot water,leathery skin,black skin,red skin,blisters",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fburns_and_scalds.mp4?alt=media&token=bb7e4eac-b568-4fc9-a7e4-9af2e6209da1",
                R.drawable.burns_and_scalds));

        dataList.add(new ContentsEmergencyData(
                "Heatstroke",
                "1",
                "heatstroke",
                "heatstroke,dizziness,sun,fever,high fever,ecstasy,sweating,headache,restless,dry skin,hot skin",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fheat_Stroke.mp4?alt=media&token=4c1b8fdc-f72a-4c1a-a493-8fcb5caa0f2f",
                R.drawable.heatstroke));


        dataList.add(new ContentsEmergencyData(
                "Water Safety",
                "2",
                "water_safety",
                "water safety,water,safety,swimming,lifeguard,water sports,flips,eye,ocean,pool,sea,drowning",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fwater_safety.mp4?alt=media&token=a1b0258c-6ebe-4394-aeb4-3bd8718cb214",
                R.drawable.water_safety));

        dataList.add(new ContentsEmergencyData(
                "Holiday Season Safety",
                "2",
                "holiday_season_safety",
                "holiday season safety,holiday,season,safety,poison,plants,wood,turkey,fryer,thanksgiving day,new year,fireplaces,burns,injuries",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fvacation_safety.mp4?alt=media&token=a2b3ac73-1368-4844-98a0-9714e423af3a",
                R.drawable.ic_holiday));

        dataList.add(new ContentsEmergencyData(
                "Vacation Safety",
                "2",
                "vacation_trip_safety",
                "vacation safety,vacation,safety,unplanned expenses,fraudulent transactions,document,passport,hotel,travel,air,plane,pickpocket,wallet",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fvacation_safety.mp4?alt=media&token=a2b3ac73-1368-4844-98a0-9714e423af3a",
                R.drawable.vacation_trip));


        dataList.add(new ContentsEmergencyData(
                "Fire Safety",
                "2",
                "fire_safety",
                "fire safety,fire,safety,fire extinguish,extinguish,hot,burn,spark,electric,fire brigade,smoke",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Ffire_safety_at_home_house_fires.mp4?alt=media&token=2e89f090-b865-437f-9c8c-e08f70bbac7b",
                R.drawable.fire));

        dataList.add(new ContentsEmergencyData(
                "Work Safety",
                "2",
                "work_safety",
                "work safety,work,safety,pins,tools,alcohol,managers,bully,harass,frustration,professionals,company,workstation,office,machinery,employee safety",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fwork_safety.mp4?alt=media&token=09ef3a4b-c47a-4791-9220-16d912f81bc1",
                R.drawable.work_safety));



        dataList.add(new ContentsEmergencyData(
                "Earthquake",
                "3",
                "earthquake",
                "earthquake,radio,shaking,shake,havoc,construction,joists,house evacuation plan,evacuation",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fearthquake.mp4?alt=media&token=7b5fa264-6cad-43d5-b481-db02f9b1731d",
                R.drawable.earthquake));

        dataList.add(new ContentsEmergencyData(
                "Flood",
                "3",
                "flood",
                "flood,floodwater,water,showers,electricity,live wire,rain",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fflood.mp4?alt=media&token=3aad739c-b209-4aa9-b0ed-4899a45cbaba",
                R.drawable.flood
        ));

        dataList.add(new ContentsEmergencyData(
                "Thunderstrom",
                "3",
                "thunderstorm",
                "thunderstrom,winds,strong winds,tornadoes,lightning,rain,rain showers",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fthunderstorm_hurricane.mp4?alt=media&token=2c548d1a-8499-4bbf-834b-3727fa6acc11",
                R.drawable.thunderstrom
        ));

        dataList.add(new ContentsEmergencyData(
                "Windstrom or Hurricane",
                "3",
                "hurricane",
                "windstrom,hurricane,weather,power,electric,fallen trees,high wind",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fthunderstorm_hurricane.mp4?alt=media&token=2c548d1a-8499-4bbf-834b-3727fa6acc11",
                R.drawable.windstrom
                ));

        dataList.add(new ContentsEmergencyData(
                "Landslide",
                "3",
                "landslide",
                "landslide,car,car damage,damage,rocks,debris,mud,mudflow,erosion,avalanches,volcano,ground,highway,mountain,tunnel",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Flandslide.mp4?alt=media&token=0f0535d6-92b9-4b34-bc58-20ac71ef46b6",
                R.drawable.landsilde));

        dataList.add(new ContentsEmergencyData(
                "Forest Fires",
                "3",
                "forest_fires",
                "forest fires,forest,fire,tree,dry leaves,spark,wildfire,wild fire,wood,smoke,burner,propane,gas,natural gas",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Fforest_fire.mp4?alt=media&token=c2d668cd-8537-4fb2-905e-1c794b7ca463",
                R.drawable.forest_fires));

        dataList.add(new ContentsEmergencyData(
                "House Fires",
                "3",
                "house_fires",
                "house fires,house,fires,electric,burners,power line,short circuit,over,indoor,fireplace,explosive,ashes,hot,burn,smoke,gas",
                "https://firebasestorage.googleapis.com/v0/b/grace-610b3.appspot.com/o/emergency_videos%2Ffire_safety_at_home_house_fires.mp4?alt=media&token=2e89f090-b865-437f-9c8c-e08f70bbac7b",
                R.drawable.house_fires));







        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                View sharedView = search;
                String transitionName = "search";
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("dataList", dataList);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent, transitionActivityOptions.toBundle());
            }
        });

        layout_emergency_numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EmergencyNumbers.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("dataList", dataList);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);
            }
        });

        layout_first_aid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FirstAid.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("dataList", dataList);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);
            }
        });

        layout_natural_disaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NaturalDisasters.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("dataList", dataList);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);
            }
        });

        layout_safety_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SafetyTips.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("dataList", dataList);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);

            }
        });



        return view;
    }


    private void init(){
        search = (ImageView) view.findViewById(R.id.search_img);

        layout_emergency_numbers = (RelativeLayout) view.findViewById(R.id.layout_emergency_numbers);
        layout_first_aid = (RelativeLayout) view.findViewById(R.id.layout_first_aid);
        layout_safety_tips = (RelativeLayout) view.findViewById(R.id.layout_safety_tips);
        layout_natural_disaster = (RelativeLayout) view.findViewById(R.id.layout_natural_disasters);

        dataList=new ArrayList<>();

    }

}

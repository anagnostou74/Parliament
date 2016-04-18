package gr.mobap.diafaneia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import gr.mobap.AndroidNetworkUtility;
import gr.mobap.MainActivity;
import gr.mobap.R;

public class Diafaneia extends MainActivity {

    private ProgressDialog pDialog;

    // URL to get data JSON
    private static String url = "http://diafaneia.hellenicparliament.gr/api.ashx?q=documents&pageSize=100";

    // JSON Node names
    private static final String TAG_Data = "Data";
    private static final String TAG_ID = "ID";
    private static final String TAG_CLEAN_URL = "CleanUrl";
    private static final String TAG_ADA = "ADA";
    private static final String TAG_ProtocolNumber = "ProtocolNumber";
    private static final String TAG_FekNo = "FekNo";
    private static final String TAG_FekIssue = "FekIssue";
    private static final String TAG_FekDate = "FekDate";
    private static final String TAG_Subject = "Subject";
    private static final String TAG_CorrectionTo = "CorrectionTo";
    private static final String TAG_RelatedDocuments = "RelatedDocuments";
    private static final String TAG_DocumentContent = "DocumentContent";
    private static final String TAG_PublishDate = "PublishDate";
    private static final String TAG_DecisionDate = "DecisionDate";
    private static final String TAG_Attachment = "Attachment";
    private static final String TAG_Att_ID = "ID";
    private static final String TAG_Att_FilePath = "FilePath";
    private static final String TAG_Att_OriginalFileName = "OriginalFileName";
    private static final String TAG_Att_SHA256 = "SHA256";
    private static final String TAG_ExpenseCategory = "ExpenseCategory";
    private static final String TAG_EXP_ID = "ID";
    private static final String TAG_EXP_TITLE = "Title";
    private static final String TAG_EXP_CleanUrl = "CleanUrl";
    private static final String TAG_ExpenseCarrierVatNumber = "ExpenseCarrierVatNumber";
    private static final String TAG_ExpenseCarrierName = "ExpenseCarrierName";
    private static final String TAG_ExpenseSponsorVatNumber = "ExpenseSponsorVatNumber";
    private static final String TAG_ExpenseSponsorName = "ExpenseSponsorName";
    private static final String TAG_ExpenseDescription = "ExpenseDescription";
    private static final String TAG_ExpenseAmount = "ExpenseAmount";
    private static final String TAG_ExpenseCPV = "ExpenseCPV";
    private static final String TAG_ExpenseKAE = "ExpenseKAE";
    private static final String TAG_BudgetCategory = "BudgetCategory";
    private static final String TAG_BudgetAmount = "BudgetAmount";
    private static final String TAG_BudgetKAE = "BudgetKAE";
    private static final String TAG_DonationAmount = "DonationAmount";
    private static final String TAG_DonationKAE = "DonationKAE";
    private static final String TAG_DonationReceiverName = "DonationReceiverName";
    private static final String TAG_DonationSupplierVatNumber = "DonationSupplierVatNumber";
    private static final String TAG_ContractAssignmentUser = "ContractAssignmentUser";
    private static final String TAG_ContractAssignmentVatNumber = "ContractAssignmentVatNumber";
    private static final String TAG_ContractKnockDownUser = "ContractKnockDownUser";
    private static final String TAG_ContractKnockDownVatNumber = "ContractKnockDownVatNumber";
    private static final String TAG_DocumentType = "DocumentType";
    private static final String TAG_DOC_ID = "ID";
    private static final String TAG_DOC_TITLE = "Title";
    private static final String TAG_DOC_CleanUrl = "CleanUrl";
    private static final String TAG_DOC_BaseCategory = "BaseCategory";
    private static final String TAG_DOC_ParentType = "ParentType";
    private static final String TAG_DOC_HierarchyPath = "HierarchyPath";
    private static final String TAG_DOC_HP_ID = "ID";
    private static final String TAG_DOC_HP_TITLE = "Title";
    private static final String TAG_Sector = "Sector";
    private static final String TAG_SEC_ID = "ID";
    private static final String TAG_SEC_TITLE = "Title";
    private static final String TAG_SEC_PARSECTOR = "ParentSector";
    private static final String TAG_SEC_CleanUrl = "CleanUrl";
    private static final String TAG_SEC_HierarchyPath = "HierarchyPath";
    private static final String TAG_SEC_HP_ID = "ID";
    private static final String TAG_SEC_HP_TITLE = "Title";
    private static final String TAG_FinalSigner = "FinalSigner";
    private static final String TAG_FSIGNER_ID = "ID";
    private static final String TAG_FSIGNER_FULLNAME = "Fullname";
    private static final String TAG_FSIGNER_INFO = "Info";
    private static final String TAG_FSIGNER_CleanUrl = "CleanUrl";
    private static final String TAG_ProtocolService = "ProtocolService";
    private static final String TAG_PROT_ID = "ID";
    private static final String TAG_PROT_TITLE = "Title";

    // data JSONArray
    JSONArray data = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diafaneia_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dataList = new ArrayList<HashMap<String, String>>();
        ListView lv = (ListView) findViewById(android.R.id.list);

        AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
        if (androidNetworkUtility.isConnected(this)) {
            // Listview on item click listener
            lv.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // getting values from selected ListItem
                    String file = ((TextView) view.findViewById(R.id.file)).getText().toString();
                    String diafUrl = "http://diafaneia.hellenicparliament.gr/" + file;
                    Intent in = new Intent(Intent.ACTION_VIEW);
                    in.putExtra(TAG_Att_FilePath, diafUrl);
                    in.setDataAndType(Uri.parse(diafUrl), "application/pdf");
                    startActivity(in);
                }
            });
            // Calling async task to get json
            new GetData().execute();
        } else {
            Toast.makeText(this, getString(R.string.aneu_diktiou), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Diafaneia.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    data = jsonObj.getJSONArray(TAG_Data);

                    // looping through All Data
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String clean_url = c.getString(TAG_CLEAN_URL);
                        String ada = c.getString(TAG_ADA);
                        String protocol = c.getString(TAG_ProtocolNumber);
                        String fekNo = c.getString(TAG_FekNo);
                        String fekIs = c.getString(TAG_FekIssue);
                        String fekDate = c.getString(TAG_FekDate);
                        String subject = c.getString(TAG_Subject);
                        String correction = c.getString(TAG_CorrectionTo);
                        String related = c.getString(TAG_RelatedDocuments);
                        String docContent = c.getString(TAG_DocumentContent);
                        String publishDate = c.getString(TAG_PublishDate);
                        String decisionDate = c.getString(TAG_DecisionDate);

                        // TAG_Attachment node is JSON Object
                        JSONObject attachment = c.getJSONObject(TAG_Attachment);
                        String attId = attachment.getString(TAG_Att_ID);
                        String attFile = attachment.getString(TAG_Att_FilePath);
                        String attOriginal = attachment.getString(TAG_Att_OriginalFileName);
                        String attSha = attachment.getString(TAG_Att_SHA256);

                        // TAG_ExpenseCategory node is JSON Object
                        //JSONObject expense = c.put(TAG_ExpenseCategory, JSONObject.NULL).optJSONObject(TAG_ExpenseCategory);

                        JSONObject expense = c.optJSONObject(TAG_ExpenseCategory);
                        if (expense == null || expense.length() == 0) {
                            expense = c.put(TAG_ExpenseCategory, JSONObject.NULL);
                        } else {
                            JSONObject exp = c.getJSONObject(TAG_ExpenseCategory);
                            String expId = exp.getString(TAG_EXP_ID);
                            String expTitle = exp.getString(TAG_EXP_TITLE);
                            String expUrl = exp.getString(TAG_EXP_CleanUrl);
                        }

                        String expCarrierVat = c.getString(TAG_ExpenseCarrierVatNumber);
                        String expCarrierNo = c.getString(TAG_ExpenseCarrierName);
                        String expSpVat = c.getString(TAG_ExpenseSponsorVatNumber);
                        String expSpName = c.getString(TAG_ExpenseSponsorName);
                        String expDesc = c.getString(TAG_ExpenseDescription);
                        String expAmount = c.getString(TAG_ExpenseAmount);
                        String expCPV = c.getString(TAG_ExpenseCPV);
                        String expKAE = c.getString(TAG_ExpenseKAE);
                        String butgetCat = c.getString(TAG_BudgetCategory);
                        String budgetAmount = c.getString(TAG_BudgetAmount);
                        String budgetKae = c.getString(TAG_BudgetKAE);
                        String donationAmount = c.getString(TAG_DonationAmount);
                        String donationKae = c.getString(TAG_DonationKAE);
                        String donationReceiv = c.getString(TAG_DonationReceiverName);
                        String donationSupplier = c.getString(TAG_DonationSupplierVatNumber);
                        String contractAssignmUser = c.getString(TAG_ContractAssignmentUser);
                        String contractAssignmVat = c.getString(TAG_ContractAssignmentVatNumber);
                        String contractKnockDownUser = c.getString(TAG_ContractKnockDownUser);
                        String contractKnockDownVat = c.getString(TAG_ContractKnockDownVatNumber);

                        // TAG_DocumentType node is JSON Object
                        JSONObject docType = c.getJSONObject(TAG_DocumentType);
                        String docId = docType.getString(TAG_DOC_ID);
                        String docTitle = docType.getString(TAG_DOC_TITLE);
                        String docUrl = docType.getString(TAG_DOC_CleanUrl);
                        String docBase = docType.getString(TAG_DOC_BaseCategory);
                        String docParent = docType.getString(TAG_DOC_ParentType);

                        JSONArray docHp = new JSONArray(data.getJSONObject(i).getJSONObject(TAG_DocumentType).optString(TAG_DOC_HierarchyPath));
                        for (int j = 0; j < docHp.length(); j++) {
                            String docHpId = docHp.getJSONObject(j).optString(TAG_DOC_HP_ID);
                            String docHpTitle = docHp.getJSONObject(j).optString(TAG_DOC_HP_TITLE);
                        }

                        // TAG_Sector node is JSON Object
                        JSONObject sector = c.getJSONObject(TAG_Sector);
                        String secId = sector.getString(TAG_SEC_ID);
                        String secTitle = sector.getString(TAG_SEC_TITLE);
                        String secPar = sector.getString(TAG_SEC_PARSECTOR);
                        String securl = sector.getString(TAG_SEC_CleanUrl);

                        JSONArray secHp = new JSONArray(data.getJSONObject(i).getJSONObject(TAG_Sector).optString(TAG_SEC_HierarchyPath));
                        for (int j = 0; j < secHp.length(); j++) {
                            String secHpId = secHp.getJSONObject(j).optString(TAG_SEC_HP_ID);
                            String secHptitle = secHp.getJSONObject(j).optString(TAG_SEC_HP_TITLE);
                        }

                        // TAG_FinalSigner node is JSON Object
                        JSONObject fsigner = c.getJSONObject(TAG_FinalSigner);
                        String fsignerId = fsigner.getString(TAG_FSIGNER_ID);
                        String fsignerName = fsigner.getString(TAG_FSIGNER_FULLNAME);
                        String fsignerInfo = fsigner.getString(TAG_FSIGNER_INFO);
                        String fsignerUrl = fsigner.getString(TAG_FSIGNER_CleanUrl);

                        // TAG_ProtocolService node is JSON Object
                        //JSONObject protocolSer = c.getJSONObject(TAG_ProtocolService);

                        JSONObject protocolSer = c.optJSONObject(TAG_ProtocolService);
                        if (protocolSer == null || protocolSer.length() == 0) {
                            protocolSer = c.put(TAG_ProtocolService, JSONObject.NULL);
                        } else {
                            JSONObject prot = c.getJSONObject(TAG_ProtocolService);
                            String protocolSerId = prot.getString(TAG_PROT_ID);
                            String protocolSerTitle = prot.getString(TAG_PROT_TITLE);
                        }

                        // tmp hashmap for single data
                        HashMap<String, String> data = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        data.put(TAG_Subject, "Τίτλος: " + subject);
                        data.put(TAG_Att_FilePath, attFile);
                        data.put(TAG_SEC_TITLE, "Υπηρεσία / Φορέας\n" + secTitle);
                        data.put(TAG_FSIGNER_INFO, "Τελικός Υπογράφων\n" + fsignerInfo);
                        data.put(TAG_FSIGNER_FULLNAME, fsignerName);

                        // adding data to data list
                        dataList.add(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    Diafaneia.this,
                    dataList,
                    R.layout.diafaneia_list_item,
                    new String[]{TAG_Subject, TAG_Att_FilePath, TAG_SEC_TITLE, TAG_FSIGNER_INFO, TAG_FSIGNER_FULLNAME},
                    new int[]{R.id.subject, R.id.file, R.id.sec_title, R.id.sinfo, R.id.signer});
            //dataList.setAdapter(adapter);
            ListView lv = (ListView) findViewById(android.R.id.list);
            lv.setAdapter(adapter);
        }

    }

}
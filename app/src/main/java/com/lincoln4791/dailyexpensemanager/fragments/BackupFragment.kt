package com.lincoln4791.dailyexpensemanager.fragments

import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.GeneratedIds
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.Repository
import com.lincoln4791.dailyexpensemanager.base.BaseFragment
import com.lincoln4791.dailyexpensemanager.calll
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.common.util.BackupUtil
import com.lincoln4791.dailyexpensemanager.common.util.NetworkCheck
import com.lincoln4791.dailyexpensemanager.common.util.Util
import com.lincoln4791.dailyexpensemanager.databinding.FragmentBackupBinding
import com.lincoln4791.dailyexpensemanager.roomDB.AppDatabase
import com.lincoln4791.dailyexpensemanager.view.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import javax.inject.Inject


@AndroidEntryPoint
class BackupFragment : BaseFragment<FragmentBackupBinding>(FragmentBackupBinding::inflate) {
    @Inject lateinit var appDatabase: AppDatabase
    @Inject lateinit var repository: Repository
    @Inject lateinit var prefManager: PrefManager
    private var user : FirebaseUser? = null
    private lateinit var navCon : NavController
    private lateinit var dialogCreatingBackup : Dialog
    private lateinit var dialogRestoringBackup : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle", "AddExpense Fragment create")
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    Log.d("tag","OnBackPressCalled -> Monthly")
                    goBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navCon = Navigation.findNavController(view)
        initUploadingContentDialog(requireContext())
        initDownloadingBackUpDialog(requireContext())
        initGoogleDrive()
        setLastBackupTime()

        binding.cvCloudBackup.setOnClickListener {

            if(NetworkCheck.isConnect(requireContext())){
                user = Firebase.auth.currentUser

                if(user!=null){
                    CoroutineScope(Dispatchers.IO).launch {
                        uploadFileToGDrive(requireContext())
                        Log.d("tag","uploading backup")

                    }
                }
                else{
                   Util.showLoginRequiredDialog(requireContext()){
                       if(it){
                           navigateToAuthActivity()
                       }
                   }
                }

            }
            else{
                Util.showNoInternetDialog(requireContext()){

                }
            }


        }

        binding.cvCloudRestore.setOnClickListener {
            if(NetworkCheck.isConnect(requireContext())){
                user = Firebase.auth.currentUser

                if(user!=null){
                        CoroutineScope(Dispatchers.IO).launch {
                            startCloudRestoreProcess()
                        }
                }
                else{
                    Util.showLoginRequiredDialog(requireContext()){
                        if(it){
                            navigateToAuthActivity()
                        }
                    }
                }

            }
            else{
                Util.showNoInternetDialog(requireContext()){

                }
            }



        }

        binding.cvLocalBackup.setOnClickListener {
            openDocumentTree()
        }

        binding.cvLocalRestore.setOnClickListener {
            restoreDBIntent()
        }

        binding.cvImg.setOnClickListener {
            goBack()
        }
    }

    private fun setLastBackupTime() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.tvLastBackupTime.text=" Last Backup Time : ${BackupUtil.getLastBackupTime(requireContext())}"
        }
    }

    private fun initUploadingContentDialog(context: Context) {
        dialogCreatingBackup = Dialog(context)
        val dialogView = layoutInflater.inflate(R.layout.dialog_content_loading,null,false)
        dialogCreatingBackup.setContentView(dialogView)
        dialogView.findViewById<TextView>(R.id.Loading).text="Database Backup is in progress...\nPlease wait!!"
        dialogCreatingBackup.setCancelable(false)
    }

    private fun initDownloadingBackUpDialog(context: Context) {
        dialogRestoringBackup = Dialog(context)
        val dialogView = layoutInflater.inflate(R.layout.dialog_content_loading,null,false)
        dialogRestoringBackup.setContentView(dialogView)
        dialogView.findViewById<TextView>(R.id.Loading).text="Database Restore is in progress...\nPlease wait!!"
        dialogRestoringBackup.setCancelable(false)
    }


    private fun goBack() {
            navCon.navigateUp()
    }


    private fun restoreDBIntent() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.type = "*/*"
        startActivityForResult(Intent.createChooser(i, "Select DB File"), RC_LOCAL_RESTORE)
    }



    private fun validFile(fileUri: Uri): Boolean {
        val cr: ContentResolver = this.requireActivity().contentResolver
        val mime = cr.getType(fileUri)
        return "application/octet-stream" == mime
        //return "application/vnd.sqlite3" == mime
    }


    private suspend fun restoreDatabase(inputStreamNewDB: InputStream?) {
        Log.d("tag","DB Restoing process started")
        appDatabase.close()
        //Delete the existing restoreFile and create a new one.
        prefManager.isDatabaseRestored=true
        BackupUtil.deleteRestoreBackupFile(requireContext())
        BackupUtil.backupDatabaseForRestore(requireContext())
        val oldDB: File = requireContext().getDatabasePath(Constants.DATABASE_NAME)
        if (inputStreamNewDB != null) {
            try {
                BackupUtil.copyFile((inputStreamNewDB as FileInputStream?)!!, FileOutputStream(oldDB))
                //Toast.makeText(requireContext(),"Restore Success",Toast.LENGTH_SHORT).show()
                /*BackupUtil.showSnackbar(findViewById(android.R.id.content),
                    getString(R.string.restore_success),
                    1)*/
                //Take the user to home screen and there we will validate if the database file was actually restored correctly.
                Handler(Looper.getMainLooper()).post {
                    dismissRestoringFileDialog()
                    BackupUtil.showRestoreSuccessDialog(requireContext())
                }


            } catch (e: IOException) {
                Toast.makeText(requireContext(),"Restore Failed ",Toast.LENGTH_SHORT).show()
                Log.d("backup", "ex for is of restore: $e")
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    dismissRestoringFileDialog()
                    BackupUtil.showRestoreFailedDialog(requireContext())
                }
            }
        } else {
            Log.d("backup", "Restore - file does not exists")
            Handler(Looper.getMainLooper()).post {
                dismissRestoringFileDialog()
                BackupUtil.showRestoreFailedDialog(requireContext())
            }
        }
    }


    private fun openDocumentTree() {
        val uriString = BackupUtil.getString(BackupUtil.FOLDER_URI, "")
        when {
            uriString == "" -> {
                Log.w("backup", "uri not stored")
                askPermissionPreDialog()
                //askPermission()
            }
            arePermissionsGranted(uriString) -> {
                makeDoc(Uri.parse(uriString))
            }
            else -> {
                Log.w("backup", "uri permission not stored")

                askPermissionPreDialog()

            }
        }
    }

    private fun askPermissionPreDialog() {
        val dialog = Dialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirm_storage_permission_required,null,false)
        dialog.setContentView(dialogView)
        dialog.show()

        dialog.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
            dialog.dismiss()
            Toast.makeText(requireContext(),"Storage permission required to save backup file.",Toast.LENGTH_SHORT).show()
        }
        dialog.findViewById<Button>(R.id.btnUnderstand).setOnClickListener {
            dialog.dismiss()
            askPermission()
        }
    }

    // this will present the user with folder browser to select a folder for our data
    private fun askPermission() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, RC_LOCAL_BACKUP)
    }


    private fun makeDoc(dirUri: Uri) {
        val dir = DocumentFile.fromTreeUri(requireContext(), dirUri)
        if (dir == null || !dir.exists()) {
            Log.e("backup", "no Dir")
            releasePermissions(dirUri)
            Toast.makeText(requireContext(),"Folder deleted, please choose another!",Toast.LENGTH_SHORT).show()
            openDocumentTree()
        } else {
            getBackupFileName {
                if(it.isNotEmpty() || it.isNotBlank()){
                    val file = dir.createFile("application/vnd.sqlite3", "$it.db")
                    if (file != null && file.canWrite()) {
                        Log.d("backup", "file.uri = ${file.uri.toString()}")
                        alterDocument(file.uri)
                    } else {
                        Log.d("backup", "no file or cannot write")
                        Toast.makeText(requireContext(),"Write error!",Toast.LENGTH_SHORT).show()

                    }
                }
            }

        }
    }


    private fun releasePermissions(uri: Uri) {
        val flags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        requireContext().contentResolver.releasePersistableUriPermission(uri,flags)
        //we should remove this uri from our shared prefs, so we can start over again next time
        BackupUtil.storeString(BackupUtil.FOLDER_URI, "")
    }


    private fun alterDocument(uri: Uri) {
        try {
            requireContext().contentResolver.openFileDescriptor(uri, "w")?.use { parcelFileDescriptor ->
                FileOutputStream(parcelFileDescriptor.fileDescriptor).use {
                    writeFile(it)
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun writeFile(fileOutputStream: FileOutputStream) {
        try {
            appDatabase.close()
            val dbfile: File = requireContext().getDatabasePath(Constants.DATABASE_NAME)
            val buffersize = 8 * 1024
            //val buffersize = 1444
            val buffer = ByteArray(buffersize)
            var bytes_read = buffersize
            val indb: InputStream = FileInputStream(dbfile)
            while (indb.read(buffer, 0, buffersize).also { bytes_read = it } > 0) {
                fileOutputStream.write(buffer, 0, bytes_read)
            }
            fileOutputStream.flush()
            indb.close()
            fileOutputStream.close()
            BackupUtil.showBackUpSuccessDialog(requireContext())

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            BackupUtil.showBackUpFailedDialog(requireContext())
            Log.d("backup", "ex for restore file: $e")
        }


    }


    private fun arePermissionsGranted(uriString: String): Boolean {
        // list of all persisted permissions for our app
        val list = requireContext().contentResolver.persistedUriPermissions
        for (i in list.indices) {
            val persistedUriString = list[i].uri.toString()
            if (persistedUriString == uriString && list[i].isWritePermission && list[i].isReadPermission) {
                return true
            }
        }
        return false
    }

    private fun getBackupFileName(callback:(fileName : String)->Unit){
        var name = ""
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.layout_input_name,null,true)

        dialog.setContentView(dialogView)

        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val ivOK = dialogView.findViewById<ImageView>(R.id.ivOk)

        dialog.show()

        ivOK.setOnClickListener {
            if(etName.text.isNullOrEmpty()){
                etName.error="Backup file name required"
            }
            else{
                name=etName.text.toString()
                dialog.dismiss()
                callback(name)
            }
        }

    }


    private fun initGoogleDrive(){

        GoogleSignIn.getLastSignedInAccount(requireContext())?.let { googleAccount ->

            val credential = GoogleAccountCredential.usingOAuth2(
                requireContext(), listOf(DriveScopes.DRIVE_FILE)
            )

            credential.selectedAccount = googleAccount.account!!

            val drive = Drive
                .Builder(
                    AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential
                )
                .setApplicationName(getString(R.string.app_name))
                .build()

        }

    }

    fun getDriveService() : Drive?{
        GoogleSignIn.getLastSignedInAccount(requireContext())?.let { googleAccount ->
            val credential = GoogleAccountCredential.usingOAuth2(
                requireContext(), listOf(DriveScopes.DRIVE_FILE)
            )
            credential.selectedAccount = googleAccount.account!!
            return Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
            )
                .setApplicationName(getString(R.string.app_name))
                .build()
        }?:Log.e("tag","last login null")
        return null
    }



    suspend fun uploadFileToGDrive(context: Context) {
        val prefManager = PrefManager(context)
        val driveService = getDriveService()
        if(driveService!=null){
            try {
                Handler(Looper.getMainLooper()).post {
                    showUploadingFileDialog()
                }
                appDatabase.close()
                val actualFfile = File("${context.getDatabasePath(Constants.DATABASE_NAME)}")
                val fID = generateAndGetIDForDriveFile(driveService)
                val gfile = com.google.api.services.drive.model.File()
                gfile.name = "IncomeExpenseManager.db"
                gfile.mimeType = "application/vnd.sqlite3"
                gfile.setId(fID)
                //gfile.id = "IncomeExpenseManager.db"
                val fileContent = FileContent("application/vnd.sqlite3", actualFfile)
                driveService.Files().create(gfile,fileContent).execute()
                Log.d("tag","file saved to drive")
                Log.d("tag","${gfile.id}")
                prefManager.driveFileID=gfile.id

                uploadDriveBackupFileIdInFirebase(gfile.id){ it: Boolean, previousBackupId: String? ->
                    if(it){
                        Handler(Looper.getMainLooper()).post {
                            dismissUploadingFileDialog()
                            BackupUtil.showBackUpSuccessDialog(context)

                            if(previousBackupId != null || previousBackupId != "null" || previousBackupId != ""){

                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        driveService.files().delete(previousBackupId).execute()
                                        Log.d("backup","previous backup deleted")
                                    }
                                    catch (e:Exception){
                                        Log.d("backup","failed previous backup deleted")
                                        e.printStackTrace()
                                    }

                                }



                            }
                            else{
                                Log.d("backup","previous backup null")
                            }

                        }
                    }
                    else{
                        Handler(Looper.getMainLooper()).post {
                            dismissUploadingFileDialog()
                            BackupUtil.showBackUpFailedDialog(context)
                        }
                    }
                }


            }catch ( e: Exception){
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    dismissUploadingFileDialog()
                    BackupUtil.showBackUpFailedDialog(context)
                }

                Log.e("tag","exception  in file save to drive -> ${e.message}")
            }
        }
        else{
            requestGoogleSignIn(RC_GOOGLESIGNIN_BACKUP)
        }

    }

    suspend fun downloadFileFromGDrive( id : String,driveService:Drive ) : File?{

        Handler(Looper.getMainLooper()).post {
            showRestoringFileDialog()
        }

        val file = File(requireContext().getExternalFilesDir("backup"), "IncomeExpenseManager.db")
        try {
            val outputStream = FileOutputStream(file)
            driveService.files()[id]
                .executeMediaAndDownloadTo(outputStream)
            /*      if (id != null) {
                      driveService.files().
                  }*/


            outputStream.flush()
            outputStream.close()
            Log.d("tag","db saveed in app specif storage")

            return file
            //startRestore(Uri.fromFile(file), CLOUD_RESTORE)

        } catch (e: Exception) {
            Log.d("tag","db save failed -> ${e.message}")
            e.printStackTrace()
            return null
        }

    }


    fun requestGoogleSignIn(requestCode: Int) {
        Log.e("tag","init google client")
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE), Scope(DriveScopes.DRIVE_APPDATA))
            .build()
        val client = GoogleSignIn.getClient(requireContext(), signInOptions)

        val signInIntent: Intent = client.signInIntent
        startActivityForResult(signInIntent, requestCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_LOCAL_BACKUP) {
                if (data != null) {
                    //this is the uri user has provided us
                    val treeUri: Uri? = data.data
                    if (treeUri != null) {
                        Log.i("backup", "got uri: ${treeUri.toString()}")
                        // here we should do some checks on the uri, we do not want root uri
                        // because it will not work on Android 11, or perhaps we have some specific
                        // folder name that we want, etc
                        if (Uri.decode(treeUri.toString()).endsWith(":")) {
                            Toast.makeText(requireContext(),
                                "Cannot use root folder!",
                                Toast.LENGTH_SHORT).show()
                            // consider asking user to select another folder
                            return
                        }
                        // here we ask the content resolver to persist the permission for us
                        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        requireContext().contentResolver.takePersistableUriPermission(treeUri,
                            takeFlags)

                        // we should store the string fo further use
                        BackupUtil.storeString(BackupUtil.FOLDER_URI, treeUri.toString())

                        //Finally, we can do our file operations
                        //Please note, that all file IO MUST be done on a background thread. It is not so in this
                        //sample - for the sake of brevity.
                        makeDoc(treeUri)
                    }
                }
            }
            else if (requestCode == RC_LOCAL_RESTORE) {
                data?.data?.also { uri ->
                    // Perform operations on the document using its URI.
                    //val fileUri = data.data
                    CoroutineScope(Dispatchers.IO).launch {
                        startRestore(uri, LOCAL_RESTORE)
                    }
                }
            }

            else if (requestCode == RC_GOOGLESIGNIN_BACKUP) {
                CoroutineScope(Dispatchers.IO).launch {
                    uploadFileToGDrive(requireContext())
                }
            }

            else if (requestCode == RC_GOOGLESIGNIN_RESTORE) {
                CoroutineScope(Dispatchers.IO).launch {
                    // if(prefManager.driveFileID !=""){
                    //getBackupFileIdsFromFirebase()

                    startCloudRestoreProcess()

                    //downloadFileFromGDrive(prefManager.driveFileID)
                    /* }
                     else{
                         Log.e("tag","drive file id is null")
                     }*/

                }

            }
        }
    }

    private suspend fun startCloudRestoreProcess() {
        getBackupFileIdsFromFirebase {
            if(it!=null){

                if(it[1]!="null"){
                    Log.d("tag","Backup files From firebase -> ${it[1]}")
                    CoroutineScope(Dispatchers.IO).launch {
                        val driveService = getDriveService()

                        if(driveService!=null){
                            //val gDrivefile = driveService.Files().get(id).execute()

                            val file = downloadFileFromGDrive(it[1],driveService)
                            if(file!=null){
                                startRestore(Uri.fromFile(file), CLOUD_RESTORE)
                            }
                            else{
                                Log.e("tag","backup file is null")
                                Handler(Looper.getMainLooper()).post {
                                    dismissRestoringFileDialog()
                                    BackupUtil.showRestoreFailedDialog(requireContext())
                                }
                            }

                        }
                        else{
                            Log.e("tag","drive service is null")
                            requestGoogleSignIn(RC_GOOGLESIGNIN_RESTORE)
                        }

                    }
                }
                else{
                    BackupUtil.showNoBackupFileFoundInFirebaseDialog(requireContext())
                }


            }
            else{
                Log.e("tag","null value when fetching file ids from firebase")
                Handler(Looper.getMainLooper()).post {
                    BackupUtil.showNoBackupFileFoundInFirebaseDialog(requireContext())
                }
            }
        }
    }

    private suspend fun startRestore(uri: Uri,restoreType : String) {
        val fileUri = uri
        try {
            if(fileUri!=null){
                val inputStream: InputStream = this.requireContext().contentResolver.openInputStream(
                    fileUri)!!

                if(restoreType== LOCAL_RESTORE){
                    if (validFile(fileUri)) {
                        Log.d("tag","db file is valid")
                        restoreDatabase(inputStream)
                    }
                    else {
                        Log.d("tag","db file is not valid")
                        Handler(Looper.getMainLooper()).post {
                            dismissRestoringFileDialog()
                            BackupUtil.showRestoreFailedDialog(requireContext())
                        }
                    }
                }
                else{
                    restoreDatabase(inputStream)
                }


                if (inputStream != null) {
                    inputStream.close()
                }
                else{
                    Log.d("tag","input stream is null")
                }
            }
            else{
                Log.d("tag","uri is empty")
                Handler(Looper.getMainLooper()).post {
                    dismissRestoringFileDialog()
                    BackupUtil.showRestoreFailedDialog(requireContext())
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("tag","xsx")
            Handler(Looper.getMainLooper()).post {
                dismissRestoringFileDialog()
                BackupUtil.showRestoreFailedDialog(requireContext())
            }
        }
    }


    private fun showUploadingFileDialog(){
        try {
            dialogCreatingBackup.show()
        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun dismissUploadingFileDialog(){
        try {
            dialogCreatingBackup.dismiss()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun showRestoringFileDialog(){
        try {
            dialogRestoringBackup.show()
        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun dismissRestoringFileDialog(){
        try {
            dialogRestoringBackup.dismiss()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }


    private fun generateAndGetIDForDriveFile(driveService:Drive):String{
        val numOfIds = 1
        var allIds: GeneratedIds? = null
        try {
            allIds = driveService.files().generateIds()
                .setSpace("drive").setCount(numOfIds).execute()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        val generatedFileIds = allIds!!.ids

        return generatedFileIds[0]
    }

    private fun navigateToAuthActivity(){
        startActivity(Intent(requireContext(), AuthActivity::class.java))
    }

    private suspend fun getBackupFileIdsFromFirebase(callback: (List<String>?) -> Unit){
        Log.d("tag","uid -> ${prefManager.UID}")
        var prevBackup = ""
        var currentBackup = ""
        Firebase.database.reference.child(Constants.USER_DATA).child(prefManager.UID).child(Constants.BACKUPS).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                     prevBackup = snapshot.child(Constants.PREVIOUS_BACKUP).value.toString()
                     currentBackup = snapshot.child(Constants.CURRENT_BACKUP).value.toString()
                    Log.d("tag","prevBackup -> $prevBackup :: current backup -> $currentBackup ")

                }
                else{
                    Log.d("backup","snapshot not exists")
                }
                callback(listOf(prevBackup,currentBackup))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("backup","backup failed -> ${error.message} ")
                callback(null)


            }
        })
    }


    suspend fun uploadDriveBackupFileIdInFirebase(id:String,callback:(isSuccess : Boolean,previousBackUpId:String?)->Unit){
        try {
            getBackupFileIdsFromFirebase{
                if(it!=null){
                    Log.d("backup","p backup -> ${it[1]} c backup -> ${id}")
                    val hashMap = HashMap<String,String>()
                    hashMap[Constants.PREVIOUS_BACKUP] = it[1]
                    hashMap[Constants.CURRENT_BACKUP] = id
                    Firebase.database.reference.child(Constants.USER_DATA).child(prefManager.UID).child(Constants.BACKUPS).updateChildren(
                        hashMap as Map<String, String>)
                    callback(true,it[1])
                }
                else{
                    callback(false,null)
                }
            }
        }
        catch (e:Exception){
            e.printStackTrace()
            callback(false,null)
        }
    }



    companion object{
        const val RC_LOCAL_BACKUP = 1
        const val RC_LOCAL_RESTORE = 2
        const val RC_GOOGLESIGNIN_BACKUP = 3
        const val RC_GOOGLESIGNIN_RESTORE = 4
        const val LOCAL_RESTORE = "local_restore"
        const val CLOUD_RESTORE = "cloud_restore"
    }

}
package com.example.projectgabi.models;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TransactionExcelModel {

    private String filePathString;
    private String fileNameString;
    private File file;
    private Context context;
    private ArrayList<String> pathHistory;
    private String lastDirectory;
    private int count = 0;
    private ArrayList<String> transactionsList;




    public TransactionExcelModel(String filePathString, String fileNameString, File file) {
        this.filePathString = filePathString;
        this.fileNameString = fileNameString;
        this.file = file;
    }

    public TransactionExcelModel() {
        transactionsList = new ArrayList<>();
    }

    public void handleImportedTransactions(String path, String fileName,Context context, Uri uri) {
        // Get the file
        File file = new File(path);
        // Get the file name

        Log.d( "Import: " ,  fileName);
        Log.d( "Import: " ,  path);
        // Get the file extension
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        // Check condition
        if (fileExtension.equals(".xls") || fileExtension.equals(".xlsx")) {

            fileNameString = fileName;

            filePathString = file.getPath();

            // read excell data and send it to the database
            readExcelData(path, context, uri);

        } else {
            // If file is not an excel file
            Toast.makeText(context, "Please import an excel file!", Toast.LENGTH_SHORT).show();
        }
    }

    private void readExcelData(String path, Context context,Uri uri) {

        // read the data from the excel file
        Log.d("Import: ", "Reading Excel File.");
        Log.d("Import: ", "Path: " + path);

        DocumentFile documentFile = DocumentFile.fromSingleUri(context,uri);
        Log.d("Import: ", "DocumentFile: " + documentFile);
        String absolutePath = documentFile.getUri().getPath();
        Log.d("Import: ", "AbsolutePath: " + absolutePath);

        File inputFile = new File(absolutePath);
        try{
            InputStream inputStream = FileUtils.openInputStream(inputFile);
            // create a workbook object
            Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook(inputStream);
            // get the first sheet of the workbook
            Sheet sheet = workbook.getSheetAt(0);
            // get the number of rows
            int rowCount = sheet.getPhysicalNumberOfRows();
            // initialize the transaction list
            transactionsList = new ArrayList<>();

            // loop through the rows
            for (int i = 0; i < rowCount; i++) {
                // get the row
                Row row = sheet.getRow(i);
                // get the number of cells
                int cellCount = row.getPhysicalNumberOfCells();
                // loop through the cells
                for (int j = 0; j < cellCount; j++) {
                    // get the cell value
                    String value = row.getCell(j).toString().trim();
                    // add the value to the list
                   // transactionsList.add(value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }

    }

    private int checkSelfPermission(String s) {
        return 0;
    }
    private void  requestPermissions(String[] strings, int i) {
    }

    public String getFilePathString() {
        return filePathString;
    }

    public void setFilePathString(String filePathString) {
        this.filePathString = filePathString;
    }

    public String getFileNameString() {
        return fileNameString;
    }

    public void setFileNameString(String fileNameString) {
        this.fileNameString = fileNameString;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

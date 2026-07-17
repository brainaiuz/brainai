package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.BarcodeGeneratorService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BarcodeItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.utils.EdsContextParams;
import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.codabar.CodabarBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code128.EAN128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.impl.fourstate.RoyalMailCBCBean;
import org.krysalis.barcode4j.impl.fourstate.USPSIntelligentMailBean;
import org.krysalis.barcode4j.impl.int2of5.ITF14Bean;
import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.impl.pdf417.PDF417Bean;
import org.krysalis.barcode4j.impl.postnet.POSTNETBean;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.impl.upcean.EAN8Bean;
import org.krysalis.barcode4j.impl.upcean.UPCABean;
import org.krysalis.barcode4j.impl.upcean.UPCEBean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Shohruh on 08 Feb 2017.
 */
@Transactional
@Service("barcodeGeneratorService")
public class BarcodeGeneratorServiceImpl implements BarcodeGeneratorService {
    private static Logger log = LoggerFactory.getLogger(BarcodeGeneratorServiceImpl.class);

    private AbstractBarcodeBean barcodeBean;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private ItemManager itemManager;

    private void initBarcodeBean(String type) {
        switch (type) {
            case Constants.BarcodeType.CODABAR -> barcodeBean = new CodabarBean();
            case Constants.BarcodeType.CODE39 -> barcodeBean = new Code39Bean();
            case Constants.BarcodeType.CODE128 -> barcodeBean = new Code128Bean();
            case Constants.BarcodeType.DATAMATRIX -> barcodeBean = new DataMatrixBean();
            case Constants.BarcodeType.EAN8 -> barcodeBean = new EAN8Bean();
            case Constants.BarcodeType.EAN13 -> barcodeBean = new EAN13Bean();
            case Constants.BarcodeType.EAN128 -> barcodeBean = new EAN128Bean();
            case Constants.BarcodeType.INTELLIGENTMAIL -> barcodeBean = new USPSIntelligentMailBean();
            case Constants.BarcodeType.INTERLEAVED2OF5 -> barcodeBean = new Interleaved2Of5Bean();
            case Constants.BarcodeType.ITF14 -> barcodeBean = new ITF14Bean();
            case Constants.BarcodeType.PDF417 -> barcodeBean = new PDF417Bean();
            case Constants.BarcodeType.POSTNET -> barcodeBean = new POSTNETBean();
            case Constants.BarcodeType.ROYALMAIL -> barcodeBean = new RoyalMailCBCBean();
            case Constants.BarcodeType.UPCA -> barcodeBean = new UPCABean();
            case Constants.BarcodeType.UPCE -> barcodeBean = new UPCEBean();
        }
    }

    public BarcodeItem generateBarcode(BarcodeItem item) {
        Integer barcodeId = null;
        EdsUploadSettings uploadSettings = null;
        if (item.getProductId() != null) {
            barcodeId = itemManager.getProductBarcode(item.getProductId());
            if (barcodeId != null) {
                uploadSettings = uploadManager.getUploadSettingsByUploadId(barcodeId);
            }
        }
        EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
        String type = numberingSettings.getBarcodeType();
        if (type == null) {
            return null;
        }
        try {
            initBarcodeBean(type);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(null, "image/png", 150, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            String formattedText = barcodeBean.generateBarcode(canvas, item.getBarcodeNumber());
            BarcodeDimension dimension = canvas.getDimensions();
            if (uploadSettings == null || !formattedText.equals(uploadSettings.getUpload().getOriginalName())) {
                barcodeId = uploadBarcodeImage(barcodeId, canvas.getBufferedImage(), formattedText);
                uploadSettings = uploadManager.getUploadSettingsByUploadId(barcodeId);
            }
            BarcodeItem barcodeItem = new BarcodeItem();
            barcodeItem.setBarcodeUrl(uploadSettings.getFileLink());
            barcodeItem.setBarcodeNumber(formattedText);
            barcodeItem.setUploadId(barcodeId);
            barcodeItem.setType(type);
            barcodeItem.setWidth(String.valueOf(dimension.getWidth()));
            barcodeItem.setHeight(String.valueOf(dimension.getHeight()));
            return barcodeItem;
        } catch (IOException | IllegalArgumentException e) {
            return null;
        }
    }

    @Transactional
    public Integer uploadBarcodeImage(Integer uploadId, BufferedImage bufferedImage, String filename) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        log.info("Upload to Amazon S3 server");
        EdsUpload upload = new EdsUpload();
        upload.setObjectID(uploadId);
        upload.setContentType("image/png");
        upload.setOriginalName(filename);

        upload.setType(referenceManager.findReference(Constants._UPLOAD_TYPE, EdsContextParams.getUploadType()));
        upload.setInputStream(is);
        try {
            if (upload.getObjectID() == null) {
                uploadManager.create(upload);
            } else {
                uploadManager.update(upload);
            }
            log.info("File Uploaded");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return upload.getObjectID();
    }
}

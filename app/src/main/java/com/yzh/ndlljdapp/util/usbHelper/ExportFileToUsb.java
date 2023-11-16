package com.yzh.ndlljdapp.util.usbHelper;

import java.io.File;

public interface ExportFileToUsb {
    /**
     * 复制 USB 文件到本地
     *
     * @param file USB文件
     */
    void copyLocalFileToUsb(final File file);
}

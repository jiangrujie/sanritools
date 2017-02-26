package com.sanri.file.split;

import java.io.File;

public interface SplitFileRenamePolicy {
	String handle(int index,File largefile,long startPosition,long fileSize);
}

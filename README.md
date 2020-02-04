# fho-csv-convertor
监控文件目录下csv文件产生，并实时转变文件编码格式

FHO导出的csv文件不带BOM头，excel打开是乱码。此工具监控目标目录下导出的CSV文件，并自动添加utf8 BOM 头，以便excel很好的识别文件

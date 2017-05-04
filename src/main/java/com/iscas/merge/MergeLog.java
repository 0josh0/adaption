package com.iscas.merge;

import java.io.*;

/**
 * Created by Summer on 2017/4/24.
 */
public class MergeLog {

    public static void merge(String filePath, String mergeLogPath) {
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                String lineTXT = null;
                // 读取微服务n的日志文件
                while ((lineTXT = bufferedReader.readLine()) != null) {
                    // String [] strs = lineTXT.split("->");
                    // 如果碰到记录服务通信情况的日志，分析。//if end with service-tag
                    if ((lineTXT.length() > 21) && lineTXT.substring(lineTXT.length() - 21, lineTXT.length())
                            .equals("ServiceInvoke-Tag————")) {
                        String[] strs = lineTXT.split("  ");
                        String id = strs[strs.length - 3];// 调用服务请求的id号
                        String content = strs[strs.length - 2];// 服务名

                        File mergeLogs = new File(mergeLogPath);// out为合并后的日志文件
                        if (!mergeLogs.exists()) {
                            try {
                                mergeLogs.createNewFile();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        // System.out.println("id = " + id);
                        // System.out.println("content = " + content);

                        doMerge(id, content, mergeLogPath);
                        // write(mergeLogPath, read(id, content, mergeLogPath));

                    }
                }
                System.out.println("Merge Log Done！");
            } else {
                System.out.println("找不到指定文件！");
            }
        } catch (Exception e) {
            System.out.println("读取文件出错！");
            e.printStackTrace();
        }
    }

    public static String doMerge(String id, String service, String mergeLogPath) {
        BufferedReader br = null;
        String line = null;
        StringBuffer buf = new StringBuffer();
        try {
            // 根据文件路径创建缓冲输入流
            br = new BufferedReader(new FileReader(mergeLogPath));
            boolean flag = false;
            // 循环读取文件的每一行, 对需要修改的行进行修改, 放入缓冲对象中
            while ((line = br.readLine()) != null) {
                // if (flag == true)
                // break;
                String[] strs2 = line.split("  ");
                // 此处根据实际需要修改某些行的内容
                if (strs2[0].equals(id)) {
                    line = line + "  " + service;
                    // buf.append(line).append(" " + service);
                    flag = true;
                }
                // 如果不用修改, 则按原来的内容回写
                // else {
                //
                // // buf.append(System.getProperty("line.separator"));
                // }
                buf.append(line);
                buf.append("\n");
            }
            if (flag == false) {
                // buf.append(System.getProperty("line.separator"));
                buf.append(id + "  " + service + "\n");
            }

            BufferedWriter bw = null;
            try {
                // 根据文件路径创建缓冲输出流
                bw = new BufferedWriter(new FileWriter(mergeLogPath));
                // 将内容写入文件中
                bw.write(buf.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 关闭流
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        bw = null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
        return buf.toString();
    }
}

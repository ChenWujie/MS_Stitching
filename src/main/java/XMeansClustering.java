import weka.clusterers.Clusterer;
import weka.clusterers.AbstractClusterer;
import weka.clusterers.XMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class XMeansClustering {
    public static void main(String[] args) throws Exception {
        HashMap<String, Double> data = null;

        try {
            // 创建文件输入流
            FileInputStream fileInputStream = new FileInputStream("hashmap.ser");
            // 创建对象输入流
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            // 反序列化HashMap
            data = (HashMap<String, Double>) objectInputStream.readObject();
            // 关闭流
            objectInputStream.close();
            fileInputStream.close();
            System.out.println("HashMap已反序列化");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 创建Weka数据集
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("value"));

        Instances dataset = new Instances("data", attributes, data.size());

        // 将HashMap数据添加到数据集中
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            double[] values = new double[1];
            values[0] = entry.getValue();
            dataset.add(new DenseInstance(1.0, values));
        }

        // 创建并配置XMeans聚类器
        XMeans xMeans = new XMeans();
        xMeans.setMinNumClusters(3); // 最小聚类数目
        xMeans.setMaxNumClusters(10); // 最大聚类数目
        xMeans.buildClusterer(dataset);

        int[] clusterSizes = new int[xMeans.numberOfClusters()];

        // 输出聚类结果
        for (int i = 0; i < dataset.numInstances(); i++) {
            int cluster = xMeans.clusterInstance(dataset.instance(i));
            System.out.println("Instance " + data.keySet().toArray()[i] + " is in cluster " + cluster);
            clusterSizes[cluster]++;
        }

        // 输出聚类数量
        System.out.println("Number of clusters: " + xMeans.numberOfClusters());
        for(int i: clusterSizes) {
            System.out.println("cluster个数" + i);
        }

    }
}

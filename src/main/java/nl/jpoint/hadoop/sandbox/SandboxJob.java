package nl.jpoint.hadoop.sandbox;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Sample Job based on https://github
 * .com/hortonworks/hadoop-tutorials/blob/master/Community/T09_Write_And_Run_Your_Own_MapReduce_Java_Program_Poll_Result_Analysis.md
 */
public class SandboxJob extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new SandboxJob(), args);
		System.exit(res);
	}

	@Override
	public int run(String[] args) throws Exception {

		Job job = Job.getInstance(new Configuration());
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(SandboxMapper.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(NullOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path("input.txt"));

		job.setJarByClass(SandboxJob.class);

		job.getConfiguration().set("mapreduce.job.user.classpath.first", "true"); //otherwise hadoop-jars will be used
		job.getConfiguration().set("mapreduce.input.fileinputformat.split.maxsize", "10");
		job.getConfiguration().set("nl.jpoint.properties", loadPropertiesAsString());

		job.waitForCompletion(true);

		return 0;
	}

	public String loadPropertiesAsString() {
		String propertiesFolder = System.getProperty("properties.folder");
		if (propertiesFolder == null) {
			throw new RuntimeException("properties.folder not set. Use " +
					"[export HADOOP_OPTS=\"$HADOOP_OPTS -Dproperties.folder=/home/hadoop/configuration\"]");
		}

		try {
			String sourcePath = propertiesFolder + "/job.properties";

			return new String(Files.readAllBytes(Paths.get(sourcePath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
package nl.jpoint.hadoop.sandbox;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

public class SandboxMapper extends Mapper<LongWritable, Text, Text, Text> {

	private static final Logger log = LoggerFactory.getLogger(SandboxMapper.class);

	@Inject
	private HelloWorldBean helloWorldBean;

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		super.setup(context);
		LazySpringContext.autowireBean(context.getConfiguration(), this);
	}

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		log.info("Hello {}.", helloWorldBean.getName());
	}

}

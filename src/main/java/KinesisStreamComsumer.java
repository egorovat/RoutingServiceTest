import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.*;

import java.util.Date;


public class KinesisStreamComsumer {

    private AmazonKinesis client;

    public KinesisStreamComsumer(AmazonKinesis client) {
        this.client = client;
    }

    public Record getRecord(String streamName, Date timestamp){

        ShardFilter filter = new ShardFilter();
        filter.setType("FROM_TIMESTAMP");
        filter.setTimestamp(timestamp);
        ListShardsRequest listShardsRequest = new ListShardsRequest().withStreamName(streamName).withShardFilter(filter);

        ListShardsResult response = client.listShards(listShardsRequest);

        Shard shard = response.getShards().get(0);
        GetShardIteratorRequest getShardIteratorRequest = new GetShardIteratorRequest();
        getShardIteratorRequest.setStreamName(streamName);
        getShardIteratorRequest.setShardId(shard.getShardId());
        getShardIteratorRequest.setShardIteratorType("TRIM_HORIZON");

        GetShardIteratorResult getShardIteratorResult =  client.getShardIterator(getShardIteratorRequest);

        String shardIterator = getShardIteratorResult.getShardIterator();
        GetRecordsRequest getRecordsRequest = new GetRecordsRequest();
        getRecordsRequest.setShardIterator(shardIterator);
        getRecordsRequest.setLimit(25);

        GetRecordsResult getRecordsResult = client.getRecords(getRecordsRequest);

        int recordsSize =  getRecordsResult.getRecords().size();
        return getRecordsResult.getRecords().get(recordsSize - 1);

    }

}

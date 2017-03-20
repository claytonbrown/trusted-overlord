var params = {
    TableName: 'overlord',
    KeySchema: [
        {
            AttributeName: 'otype',
            KeyType: 'HASH',
        },
        {
            AttributeName: 'odate',
            KeyType: 'RANGE',
        }
    ],
    AttributeDefinitions: [
        {
            AttributeName: 'otype',
            AttributeType: 'S',
        },
        {
            AttributeName: 'odate',
            AttributeType: 'S',
        },
    ],
    ProvisionedThroughput: {
        ReadCapacityUnits: 1,
        WriteCapacityUnits: 1,
    }
};
dynamodb.createTable(params, function(err, data) {
    if (err) ppJson(err); // an error occurred
    else ppJson(data); // successful response

});
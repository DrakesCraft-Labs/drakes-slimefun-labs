"""
    print("\n" + "="*80)
    print("SALESFORCE TOOLKIT - DATA SYNC PIPELINE")
    print("="*80 + "\n")

    # 1. Authenticate
    print("1. Authenticating...")
    auth = JWTAuthenticator.from_env()
    session = auth.authenticate()
    client = SalesforceClient(session, logger_instance=logger)
    print("✓ Authenticated\n")

    # 2. Prepare source data (e.g., from database, CSV, API)
    print("2. Preparing source data...")
    source_data = [
        {
            "customer_id": "CUST-001",
            "customer_name": "Tech Innovations Inc",
            "customer_email": "contact@techinnovations.com",
            "customer_phone": "555-1001",
            "customer_industry": "technology",
            "address_city": "Seattle",
            "address_state": "WA"
        },
        {
            "customer_id": "CUST-002",
            "customer_name": "Global Manufacturing Co",
            "customer_email": "info@globalmanufacturing.com",
            "customer_phone": "555-1002",
            "customer_industry": "manufacturing",
            "address_city": "Detroit",
            "address_state": "MI"
        },
        {
            "customer_id": "CUST-003",
            "customer_name": "Healthcare Solutions LLC",
            "customer_email": "hello@healthcaresolutions.com",
            "customer_phone": "555-1003",
            "customer_industry": "healthcare",
            "address_city": "Boston",
            "address_state": "MA"
        }
    ]
    print(f"✓ Prepared {len(source_data)} source records\n")

    # 3. Define field mapping
    print("3. Defining field mapping...")
    mapper = FieldMapper({
        "customer_name": "Name",
        "customer_email": "Email",
        "customer_phone": "Phone",
        "customer_industry": ("Industry", lambda x: x.title()),  # Transform: lowercase -> Title Case
        "address_city": "BillingCity",
        "address_state": "BillingState"
    })
    print("✓ Field mapping configured\n")

    # 4. Create sync pipeline
    print("4. Creating sync pipeline...")
    pipeline = SyncPipeline(
        client=client,
        sobject="Account",
        mapper=mapper,
        mode=SyncMode.INSERT,  # INSERT mode (create new records)
        batch_size=100,
        stop_on_error=False  # Continue on errors
    )
    print("✓ Pipeline created\n")

    # 5. Add callbacks for progress tracking (optional)
    def on_record_success(record, salesforce_id):
        logger.info(f"  ✓ Synced: {record['customer_name']} -> {salesforce_id}")

    def on_record_error(record, error):
        logger.error(f"  ✗ Failed: {record['customer_name']} - {error}")

    def on_batch_complete(batch_num, total_batches, result):
        logger.info(f"  Batch {batch_num}/{total_batches} complete ({result.success_count} success, {result.error_count} errors)")

    pipeline.callbacks = {
        "on_record_success": on_record_success,
        "on_record_error": on_record_error,
        "on_batch_complete": on_batch_complete
    }

    # 6. Run sync
    print("5. Running sync...")
    print("-" * 80)
    result = pipeline.sync(source_data)
    print("-" * 80 + "\n")

    # 7. Display results
    print("6. Sync Results:")
    print(f"  Status: {result.status.value}")
    print(f"  Total Records: {result.total_records}")
    print(f"  Successful: {result.success_count}")
    print(f"  Errors: {result.error_count}")
    print(f"  Success Rate: {result.success_rate:.1f}%")
    print(f"  Duration: {result.metadata['elapsed_seconds']}s")
    print(f"  Records/sec: {result.metadata['records_per_second']:.2f}")

    if result.salesforce_ids:
        print(f"\n  Created Salesforce IDs:")
        for sf_id in result.salesforce_ids:
            print(f"    - {sf_id}")

    if result.errors:
        print(f"\n  Errors:")
        for error in result.errors:
            print(f"    - {error['error']}")

    print("\n" + "="*80)
    print("✓ Data sync completed!")
    print("="*80 + "\n")


if __name__ == "__main__":
    main()

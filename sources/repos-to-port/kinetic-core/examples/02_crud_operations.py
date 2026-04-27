"""
    print("\n" + "="*80)
    print("SALESFORCE TOOLKIT - CRUD OPERATIONS")
    print("="*80 + "\n")

    # 1. Authenticate
    print("1. Authenticating...")
    auth = JWTAuthenticator.from_env()
    session = auth.authenticate()
    client = SalesforceClient(session, logger_instance=logger)
    print("✓ Authenticated\n")

    # 2. CREATE - Create a new Account
    print("2. CREATE - Creating new Account...")
    account_data = {
        "Name": "ACME Corporation",
        "Industry": "Technology",
        "Phone": "555-0100",
        "Website": "https://acme.example.com",
        "BillingCity": "San Francisco",
        "BillingState": "CA",
        "BillingCountry": "USA"
    }

    account_id = client.create("Account", account_data)
    print(f"✓ Created Account: {account_id}\n")

    # 3. READ - Query the created Account
    print("3. READ - Querying Account...")
    account = client.get("Account", account_id, fields=["Id", "Name", "Industry", "Phone"])
    print(f"✓ Retrieved Account:")
    print(f"  ID: {account['Id']}")
    print(f"  Name: {account['Name']}")
    print(f"  Industry: {account['Industry']}")
    print(f"  Phone: {account['Phone']}\n")

    # 4. QUERY - Query multiple Accounts
    print("4. QUERY - Finding all Technology Accounts...")
    query = "SELECT Id, Name, Industry FROM Account WHERE Industry = 'Technology' LIMIT 5"
    accounts = client.query(query)
    print(f"✓ Found {len(accounts)} Technology Accounts:")
    for acc in accounts:
        print(f"  - {acc['Name']} ({acc['Id']})")
    print()

    # 5. UPDATE - Update the Account
    print("5. UPDATE - Updating Account...")
    update_data = {
        "Phone": "555-0999",
        "Industry": "Manufacturing"
    }
    client.update("Account", account_id, update_data)
    print(f"✓ Updated Account: {account_id}\n")

    # Verify update
    updated_account = client.get("Account", account_id, fields=["Id", "Name", "Industry", "Phone"])
    print(f"  Updated Industry: {updated_account['Industry']}")
    print(f"  Updated Phone: {updated_account['Phone']}\n")

    # 6. COUNT - Count records
    print("6. COUNT - Counting Accounts...")
    total_accounts = client.count("Account")
    tech_accounts = client.count("Account", "Industry = 'Technology'")
    print(f"✓ Total Accounts: {total_accounts}")
    print(f"✓ Technology Accounts: {tech_accounts}\n")

    # 7. UPSERT - Insert or Update using external ID
    print("7. UPSERT - Upserting Account (requires External_Key__c field)...")
    # Note: This requires an External ID field to exist in Salesforce
    # Uncomment if you have External_Key__c field configured:
    #
    # upsert_data = {
    #     "Name": "Globex Corporation",
    #     "Industry": "Technology"
    # }
    # upsert_id = client.upsert(
    #     "Account",
    #     "External_Key__c",
    #     "EXT-12345",
    #     upsert_data
    # )
    # print(f"✓ Upserted Account: {upsert_id}\n")
    print("  (Skipped - requires External_Key__c custom field)\n")

    # 8. DELETE - Delete the created Account
    print("8. DELETE - Deleting test Account...")
    response = input(f"Delete Account {account_id}? (yes/no): ")

    if response.lower() == 'yes':
        client.delete("Account", account_id)
        print(f"✓ Deleted Account: {account_id}\n")
    else:
        print(f"  Account {account_id} was not deleted\n")

    print("="*80)
    print("✓ CRUD operations completed successfully!")
    print("="*80 + "\n")


if __name__ == "__main__":
    main()

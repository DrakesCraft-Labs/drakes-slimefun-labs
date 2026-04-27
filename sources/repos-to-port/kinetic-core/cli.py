#!/usr/bin/env python3
"""
Salesforce Toolkit - Command Line Interface

Provides easy access to Salesforce operations from the command line.

Usage:
    sf-toolkit auth --method jwt
    sf-toolkit query "SELECT Id, Name FROM Account LIMIT 10"
    sf-toolkit create Account --data '{"Name": "ACME Corp"}'
    sf-toolkit sync --config sync_config.yaml
"""

import argparse
import sys
import json
import logging
from pathlib import Path
from typing import Optional

from kinetic_core import (
    JWTAuthenticator,
    OAuthAuthenticator,
    SalesforceClient,
    FieldMapper,
    SyncPipeline,
    SyncMode
)
from kinetic_core.logging import setup_logger


logger = setup_logger("sf-toolkit-cli", console_colors=True)


def cmd_auth(args):
    """Test authentication and display connection info."""
    try:
        logger.info(f"Authenticating with method: {args.method}")

        if args.method == "jwt":
            auth = JWTAuthenticator.from_env()
        elif args.method == "oauth":
            auth = OAuthAuthenticator.from_env()
        else:
            logger.error(f"Unknown auth method: {args.method}")
            sys.exit(1)

        session = auth.authenticate()

        logger.info("✓ Authentication successful!")
        print(f"\nInstance URL: {session.instance_url}")
        print(f"API Version: {session.api_version}")
        print(f"Username: {session.username}")
        print(f"Access Token: {session.access_token[:20]}...")

    except Exception as e:
        logger.error(f"✗ Authentication failed: {e}")
        sys.exit(1)


def cmd_query(args):
    """Execute a SOQL query."""
    try:
        # Authenticate
        auth = _get_authenticator(args.auth_method)
        session = auth.authenticate()
        client = SalesforceClient(session)

        # Execute query
        logger.info(f"Executing query: {args.soql}")
        results = client.query(args.soql)

        # Output results
        if args.output == "json":
            print(json.dumps(results, indent=2))
        elif args.output == "table":
            _print_table(results)
        elif args.output == "count":
            print(f"Total records: {len(results)}")
        else:
            for record in results:
                print(record)

        logger.info(f"✓ Query returned {len(results)} records")

    except Exception as e:
        logger.error(f"✗ Query failed: {e}")
        sys.exit(1)


def cmd_create(args):
    """Create a Salesforce record."""
    try:
        # Parse data
        if args.data:
            data = json.loads(args.data)
        elif args.file:
            with open(args.file, 'r') as f:
                data = json.load(f)
        else:
            logger.error("Either --data or --file must be provided")
            sys.exit(1)

        # Authenticate
        auth = _get_authenticator(args.auth_method)
        session = auth.authenticate()
        client = SalesforceClient(session)

        # Create record
        logger.info(f"Creating {args.sobject} record...")
        record_id = client.create(args.sobject, data)

        print(f"✓ Created {args.sobject}: {record_id}")
        logger.info(f"✓ Created {args.sobject}: {record_id}")

    except Exception as e:
        logger.error(f"✗ Create failed: {e}")
        sys.exit(1)


def cmd_update(args):
    """Update a Salesforce record."""
    try:
        # Parse data
        if args.data:
            data = json.loads(args.data)
        elif args.file:
            with open(args.file, 'r') as f:
                data = json.load(f)
        else:
            logger.error("Either --data or --file must be provided")
            sys.exit(1)

        # Authenticate
        auth = _get_authenticator(args.auth_method)
        session = auth.authenticate()
        client = SalesforceClient(session)

        # Update record
        logger.info(f"Updating {args.sobject}/{args.record_id}...")
        client.update(args.sobject, args.record_id, data)

        print(f"✓ Updated {args.sobject}/{args.record_id}")
        logger.info(f"✓ Updated {args.sobject}/{args.record_id}")

    except Exception as e:
        logger.error(f"✗ Update failed: {e}")
        sys.exit(1)


def cmd_delete(args):
    """Delete a Salesforce record."""
    try:
        # Authenticate
        auth = _get_authenticator(args.auth_method)
        session = auth.authenticate()
        client = SalesforceClient(session)

        # Delete record
        logger.info(f"Deleting {args.sobject}/{args.record_id}...")
        client.delete(args.sobject, args.record_id)

        print(f"✓ Deleted {args.sobject}/{args.record_id}")
        logger.info(f"✓ Deleted {args.sobject}/{args.record_id}")

    except Exception as e:
        logger.error(f"✗ Delete failed: {e}")
        sys.exit(1)


def cmd_sync(args):
    """Run a data synchronization pipeline."""
    try:
        import yaml

        # Load config
        config_path = Path(args.config)
        if not config_path.exists():
            logger.error(f"Config file not found: {args.config}")
            sys.exit(1)

        with open(config_path, 'r') as f:
            config = yaml.safe_load(f)

        # Authenticate
        auth = _get_authenticator(args.auth_method)
        session = auth.authenticate()
        client = SalesforceClient(session)

        # Load source data
        source_data = _load_source_data(config.get("source"))

        # Create pipeline
        pipeline = SyncPipeline.from_config(config.get("pipeline"), client)

        # Run sync
        logger.info(f"Starting sync: {len(source_data)} records")
        result = pipeline.sync(source_data)

        # Display results
        print(f"\n{result}")
        print(f"Success Rate: {result.success_rate:.1f}%")

        if result.errors and args.show_errors:
            print("\nErrors:")
            for error in result.errors[:10]:  # Show first 10 errors
                print(f"  - {error}")

        if result.status.value == "success":
            logger.info("✓ Sync completed successfully")
            sys.exit(0)
        else:
            logger.warning(f"⚠ Sync completed with status: {result.status.value}")
            sys.exit(1 if result.status.value == "failed" else 0)

    except Exception as e:
        logger.error(f"✗ Sync failed: {e}", exc_info=True)
        sys.exit(1)


def cmd_describe(args):
    """Describe a Salesforce object."""
    try:
        # Authenticate
        auth = _get_authenticator(args.auth_method)
        session = auth.authenticate()
        client = SalesforceClient(session)

        # Describe object
        logger.info(f"Describing {args.sobject}...")
        metadata = client.describe(args.sobject)

        if args.output == "json":
            print(json.dumps(metadata, indent=2))
        else:
            print(f"\nObject: {metadata['name']}")
            print(f"Label: {metadata['label']}")
            print(f"Custom: {metadata['custom']}")
            print(f"Queryable: {metadata['queryable']}")
            print(f"Createable: {metadata['createable']}")
            print(f"Updateable: {metadata['updateable']}")
            print(f"Deletable: {metadata['deletable']}")

            if args.fields:
                print(f"\nFields ({len(metadata['fields'])}):")
                for field in metadata['fields'][:20]:  # Show first 20 fields
                    print(f"  - {field['name']} ({field['type']}) - {field['label']}")

        logger.info(f"✓ Described {args.sobject}")

    except Exception as e:
        logger.error(f"✗ Describe failed: {e}")
        sys.exit(1)


def _get_authenticator(method: str):
    """Get authenticator based on method."""
    if method == "jwt":
        return JWTAuthenticator.from_env()
    elif method == "oauth":
        return OAuthAuthenticator.from_env()
    else:
        raise ValueError(f"Unknown auth method: {method}")


def _load_source_data(source_config: dict) -> list:
    """Load source data based on configuration."""
    source_type = source_config.get("type")

    if source_type == "json":
        with open(source_config["path"], 'r') as f:
            return json.load(f)

    elif source_type == "csv":
        import csv
        with open(source_config["path"], 'r') as f:
            reader = csv.DictReader(f)
            return list(reader)

    elif source_type == "database":
        # Placeholder for database source
        raise NotImplementedError("Database source not yet implemented")

    else:
        raise ValueError(f"Unknown source type: {source_type}")


def _print_table(records: list):
    """Print records in table format."""
    if not records:
        print("No records found")
        return

    # Get all field names
    fields = list(records[0].keys())

    # Calculate column widths
    widths = {field: len(field) for field in fields}
    for record in records:
        for field in fields:
            value = str(record.get(field, ''))
            widths[field] = max(widths[field], len(value))

    # Print header
    header = " | ".join(f"{field:<{widths[field]}}" for field in fields)
    print(header)
    print("-" * len(header))

    # Print rows
    for record in records:
        row = " | ".join(f"{str(record.get(field, '')):<{widths[field]}}" for field in fields)
        print(row)


def main():
    """Main CLI entry point."""
    parser = argparse.ArgumentParser(
        description="Salesforce Toolkit - Command Line Interface",
        formatter_class=argparse.RawDescriptionHelpFormatter
    )

    parser.add_argument(
        "--auth-method",
        choices=["jwt", "oauth"],
        default="jwt",
        help="Authentication method (default: jwt)"
    )

    parser.add_argument(
        "--log-level",
        choices=["DEBUG", "INFO", "WARNING", "ERROR"],
        default="INFO",
        help="Logging level (default: INFO)"
    )

    subparsers = parser.add_subparsers(dest="command", help="Available commands")

    # Auth command
    auth_parser = subparsers.add_parser("auth", help="Test authentication")
    auth_parser.add_argument("--method", choices=["jwt", "oauth"], default="jwt", help="Auth method")
    auth_parser.set_defaults(func=cmd_auth)

    # Query command
    query_parser = subparsers.add_parser("query", help="Execute SOQL query")
    query_parser.add_argument("soql", help="SOQL query string")
    query_parser.add_argument("--output", choices=["json", "table", "count", "raw"], default="table", help="Output format")
    query_parser.set_defaults(func=cmd_query)

    # Create command
    create_parser = subparsers.add_parser("create", help="Create a record")
    create_parser.add_argument("sobject", help="Salesforce object name")
    create_parser.add_argument("--data", help="JSON data string")
    create_parser.add_argument("--file", help="JSON file path")
    create_parser.set_defaults(func=cmd_create)

    # Update command
    update_parser = subparsers.add_parser("update", help="Update a record")
    update_parser.add_argument("sobject", help="Salesforce object name")
    update_parser.add_argument("record_id", help="Salesforce record ID")
    update_parser.add_argument("--data", help="JSON data string")
    update_parser.add_argument("--file", help="JSON file path")
    update_parser.set_defaults(func=cmd_update)

    # Delete command
    delete_parser = subparsers.add_parser("delete", help="Delete a record")
    delete_parser.add_argument("sobject", help="Salesforce object name")
    delete_parser.add_argument("record_id", help="Salesforce record ID")
    delete_parser.set_defaults(func=cmd_delete)

    # Sync command
    sync_parser = subparsers.add_parser("sync", help="Run data synchronization")
    sync_parser.add_argument("--config", required=True, help="Path to sync configuration YAML file")
    sync_parser.add_argument("--show-errors", action="store_true", help="Display errors")
    sync_parser.set_defaults(func=cmd_sync)

    # Describe command
    describe_parser = subparsers.add_parser("describe", help="Describe a Salesforce object")
    describe_parser.add_argument("sobject", help="Salesforce object name")
    describe_parser.add_argument("--output", choices=["json", "summary"], default="summary", help="Output format")
    describe_parser.add_argument("--fields", action="store_true", help="Show field list")
    describe_parser.set_defaults(func=cmd_describe)

    # Parse arguments
    args = parser.parse_args()

    # Set log level
    logging.getLogger().setLevel(getattr(logging, args.log_level))

    # Execute command
    if hasattr(args, 'func'):
        args.func(args)
    else:
        parser.print_help()


if __name__ == "__main__":
    main()

"""
Kinetic Core - Salesforce Integration Library
Copyright (c) 2025 Antonio Trento (https://antoniotrento.net)

This file is part of Kinetic Core, the foundational library powering KineticMCP.

Project: https://github.com/antonio-backend-projects/kinetic-core
Website: https://kineticmcp.com
Author: Antonio Trento
License: MIT (see LICENSE file for details)

Part of the KineticMCP ecosystem - AI-powered Salesforce integration tools.
"""

"""
Logging System for Salesforce Toolkit.

Provides comprehensive logging with:
- Console and file output
- Log rotation
- Structured logging
- Multiple log levels
- Contextual information
"""

import os
import logging
import sys
from pathlib import Path
from logging.handlers import RotatingFileHandler
from typing import Optional
from datetime import datetime


class SalesforceToolkitLogger:
    """
    Centralized logging system for Salesforce Toolkit.

    Features:
    - Automatic log rotation
    - Colored console output (optional)
    - File and console handlers
    - Structured log format
    - Per-module loggers

    Example:
        ```python
        from kinetic_core.logging import setup_logger

        logger = setup_logger("my_script")
        logger.info("Starting sync")
        logger.error("Sync failed", exc_info=True)
        ```
    """

    # ANSI color codes for console output
    COLORS = {
        'DEBUG': '\033[36m',     # Cyan
        'INFO': '\033[32m',      # Green
        'WARNING': '\033[33m',   # Yellow
        'ERROR': '\033[31m',     # Red
        'CRITICAL': '\033[35m',  # Magenta
        'RESET': '\033[0m'       # Reset
    }

    @classmethod
    def setup(
        cls,
        name: str = "kinetic_core",
        log_dir: Optional[str] = None,
        log_level: int = logging.INFO,
        max_bytes: int = 10 * 1024 * 1024,  # 10 MB
        backup_count: int = 5,
        console_output: bool = True,
        console_colors: bool = True,
        log_format: Optional[str] = None,
    ) -> logging.Logger:
        """
        Setup and configure a logger.

        Args:
            name: Logger name (used for log file name and logger identifier)
            log_dir: Directory for log files (default: ./logs)
            log_level: Logging level (DEBUG, INFO, WARNING, ERROR, CRITICAL)
            max_bytes: Max size of log file before rotation
            backup_count: Number of backup files to keep
            console_output: If True, also log to console
            console_colors: If True, use colored output in console
            log_format: Custom log format string

        Returns:
            logging.Logger: Configured logger

        Example:
            ```python
            logger = SalesforceToolkitLogger.setup(
                name="my_app",
                log_dir="/var/logs/salesforce",
                log_level=logging.DEBUG,
                console_colors=True
            )

            logger.info("Application started")
            ```
        """
        # Create logger
        logger = logging.getLogger(name)
        logger.setLevel(log_level)

        # Avoid duplicate handlers
        if logger.handlers:
            return logger

        # Default log format
        if log_format is None:
            log_format = (
                '%(asctime)s - %(name)s - %(levelname)s - '
                '%(filename)s:%(lineno)d - %(message)s'
            )

        date_format = '%Y-%m-%d %H:%M:%S'

        # Create formatter
        formatter = logging.Formatter(log_format, datefmt=date_format)

        # File handler with rotation
        if log_dir:
            log_path = Path(log_dir)
            log_path.mkdir(parents=True, exist_ok=True)

            log_file = log_path / f"{name}.log"
            file_handler = RotatingFileHandler(
                log_file,
                maxBytes=max_bytes,
                backupCount=backup_count,
                encoding='utf-8'
            )
            file_handler.setLevel(log_level)
            file_handler.setFormatter(formatter)
            logger.addHandler(file_handler)

        # Console handler
        if console_output:
            console_handler = logging.StreamHandler(sys.stdout)
            console_handler.setLevel(log_level)

            if console_colors and cls._supports_color():
                console_formatter = cls._ColoredFormatter(log_format, datefmt=date_format)
                console_handler.setFormatter(console_formatter)
            else:
                console_handler.setFormatter(formatter)

            logger.addHandler(console_handler)

        logger.info(f"Logger '{name}' initialized (level: {logging.getLevelName(log_level)})")

        return logger

    @classmethod
    def _supports_color(cls) -> bool:
        """
        Check if terminal supports ANSI colors.

        Returns:
            bool: True if colors are supported
        """
        # Check if running in a terminal that supports colors
        return (
            hasattr(sys.stdout, 'isatty') and
            sys.stdout.isatty() and
            os.getenv('TERM') not in (None, 'dumb')
        )

    class _ColoredFormatter(logging.Formatter):
        """Custom formatter with colored output."""

        def format(self, record: logging.LogRecord) -> str:
            """Format log record with colors."""
            levelname = record.levelname
            color = SalesforceToolkitLogger.COLORS.get(levelname, '')
            reset = SalesforceToolkitLogger.COLORS['RESET']

            # Add color to levelname
            original_levelname = record.levelname
            record.levelname = f"{color}{levelname}{reset}"

            # Format message
            formatted = super().format(record)

            # Restore original levelname
            record.levelname = original_levelname

            return formatted


def setup_logger(
    name: str = "salesforce_toolkit",
    log_dir: Optional[str] = None,
    log_level: int = logging.INFO,
    **kwargs
) -> logging.Logger:
    """
    Convenience function to setup a logger.

    This is a shortcut for SalesforceToolkitLogger.setup().

    Args:
        name: Logger name
        log_dir: Log directory (default: ./logs or env LOG_DIR)
        log_level: Logging level
        **kwargs: Additional arguments for SalesforceToolkitLogger.setup()

    Returns:
        logging.Logger: Configured logger

    Example:
        ```python
        from salesforce_toolkit.logging import setup_logger

        logger = setup_logger("my_script", log_level=logging.DEBUG)
        logger.info("Starting...")
        ```
    """
    # Get log dir from environment if not provided
    if log_dir is None:
        log_dir = os.getenv("LOG_DIR", "./logs")

    return SalesforceToolkitLogger.setup(
        name=name,
        log_dir=log_dir,
        log_level=log_level,
        **kwargs
    )


def get_logger(name: str) -> logging.Logger:
    """
    Get an existing logger by name.

    Args:
        name: Logger name

    Returns:
        logging.Logger: Logger instance

    Example:
        ```python
        logger = get_logger("salesforce_toolkit")
        logger.info("Using existing logger")
        ```
    """
    return logging.getLogger(name)


class ContextLogger:
    """
    Logger with contextual information.

    Allows adding context (e.g., transaction ID, user ID) to all log messages.

    Example:
        ```python
        logger = setup_logger("my_app")
        context_logger = ContextLogger(logger, context={"transaction_id": "12345"})

        context_logger.info("Processing record")
        # Logs: "Processing record [transaction_id=12345]"
        ```
    """

    def __init__(self, logger: logging.Logger, context: Optional[dict] = None):
        """
        Initialize context logger.

        Args:
            logger: Base logger instance
            context: Context dictionary to add to all log messages
        """
        self.logger = logger
        self.context = context or {}

    def _format_message(self, message: str) -> str:
        """Format message with context."""
        if not self.context:
            return message

        context_str = ", ".join(f"{k}={v}" for k, v in self.context.items())
        return f"{message} [{context_str}]"

    def debug(self, message: str, *args, **kwargs):
        """Log debug message with context."""
        self.logger.debug(self._format_message(message), *args, **kwargs)

    def info(self, message: str, *args, **kwargs):
        """Log info message with context."""
        self.logger.info(self._format_message(message), *args, **kwargs)

    def warning(self, message: str, *args, **kwargs):
        """Log warning message with context."""
        self.logger.warning(self._format_message(message), *args, **kwargs)

    def error(self, message: str, *args, **kwargs):
        """Log error message with context."""
        self.logger.error(self._format_message(message), *args, **kwargs)

    def critical(self, message: str, *args, **kwargs):
        """Log critical message with context."""
        self.logger.critical(self._format_message(message), *args, **kwargs)

    def add_context(self, **kwargs):
        """Add context fields."""
        self.context.update(kwargs)

    def clear_context(self):
        """Clear all context fields."""
        self.context.clear()


def configure_logging_from_env():
    """
    Configure logging from environment variables.

    Environment variables:
        - LOG_LEVEL: Logging level (DEBUG, INFO, WARNING, ERROR, CRITICAL)
        - LOG_DIR: Log directory path
        - LOG_CONSOLE_OUTPUT: Enable console output (true/false)
        - LOG_CONSOLE_COLORS: Enable colored console output (true/false)

    Example:
        ```bash
        export LOG_LEVEL=DEBUG
        export LOG_DIR=/var/logs/salesforce
        export LOG_CONSOLE_COLORS=true
        ```
    """
    log_level_str = os.getenv("LOG_LEVEL", "INFO").upper()
    log_level = getattr(logging, log_level_str, logging.INFO)

    log_dir = os.getenv("LOG_DIR", "./logs")
    console_output = os.getenv("LOG_CONSOLE_OUTPUT", "true").lower() == "true"
    console_colors = os.getenv("LOG_CONSOLE_COLORS", "true").lower() == "true"

    setup_logger(
        name="salesforce_toolkit",
        log_dir=log_dir,
        log_level=log_level,
        console_output=console_output,
        console_colors=console_colors
    )

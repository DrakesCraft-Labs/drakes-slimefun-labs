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
Job status polling with exponential backoff.
"""

import time
from typing import Callable, Optional
from .job import BulkJob


class JobPoller:
    """Handles polling for Bulk API v2 job completion."""

    def __init__(
        self,
        initial_delay: float = 1.0,
        max_delay: float = 30.0,
        backoff_factor: float = 2.0,
        timeout: Optional[float] = None
    ):
        """
        Initialize job poller.

        Args:
            initial_delay: Initial polling interval in seconds (default: 1.0)
            max_delay: Maximum polling interval in seconds (default: 30.0)
            backoff_factor: Exponential backoff multiplier (default: 2.0)
            timeout: Maximum time to wait in seconds (default: None for unlimited)
        """
        self.initial_delay = initial_delay
        self.max_delay = max_delay
        self.backoff_factor = backoff_factor
        self.timeout = timeout

    def poll(
        self,
        check_status: Callable[[], BulkJob],
        on_progress: Optional[Callable[[BulkJob], None]] = None
    ) -> BulkJob:
        """
        Poll job status until completion.

        Args:
            check_status: Function that returns current job status
            on_progress: Optional callback called on each status check

        Returns:
            Final BulkJob when complete

        Raises:
            TimeoutError: If timeout is exceeded
            RuntimeError: If job fails or is aborted

        Example:
            >>> poller = JobPoller(timeout=300)  # 5 minutes
            >>> final_job = poller.poll(
            ...     check_status=lambda: client.get_job_status(job_id),
            ...     on_progress=lambda job: print(f"Progress: {job.state}")
            ... )
        """
        start_time = time.time()
        current_delay = self.initial_delay
        attempts = 0

        while True:
            attempts += 1

            # Check job status
            job = check_status()

            # Call progress callback
            if on_progress:
                on_progress(job)

            # Check if complete
            if job.is_complete():
                if job.is_successful():
                    return job
                elif job.state == 'Failed':
                    raise RuntimeError(f"Bulk job {job.id} failed after {attempts} attempts")
                elif job.state == 'Aborted':
                    raise RuntimeError(f"Bulk job {job.id} was aborted after {attempts} attempts")

            # Check timeout
            if self.timeout:
                elapsed = time.time() - start_time
                if elapsed >= self.timeout:
                    raise TimeoutError(
                        f"Job {job.id} did not complete within {self.timeout} seconds. "
                        f"Current state: {job.state}, Attempts: {attempts}"
                    )

            # Wait before next poll
            time.sleep(current_delay)

            # Exponential backoff
            current_delay = min(current_delay * self.backoff_factor, self.max_delay)

    def poll_async(
        self,
        job_id: str,
        check_status: Callable[[str], BulkJob],
        on_complete: Optional[Callable[[BulkJob], None]] = None,
        on_error: Optional[Callable[[Exception], None]] = None
    ) -> None:
        """
        Poll job status asynchronously (non-blocking).

        Note: This is a simplified async implementation. For true async support,
        consider using asyncio or threading.

        Args:
            job_id: Bulk job ID to poll
            check_status: Function that takes job_id and returns current status
            on_complete: Callback when job completes successfully
            on_error: Callback when job fails or errors occur
        """
        try:
            final_job = self.poll(lambda: check_status(job_id))
            if on_complete:
                on_complete(final_job)
        except Exception as e:
            if on_error:
                on_error(e)
            else:
                raise


def create_default_poller(timeout_minutes: Optional[float] = None) -> JobPoller:
    """
    Create a JobPoller with recommended default settings.

    Args:
        timeout_minutes: Timeout in minutes (default: None for unlimited)

    Returns:
        Configured JobPoller instance
    """
    timeout_seconds = timeout_minutes * 60 if timeout_minutes else None

    return JobPoller(
        initial_delay=2.0,  # Start with 2 second delay
        max_delay=30.0,     # Max 30 seconds between polls
        backoff_factor=1.5,  # Gradual backoff
        timeout=timeout_seconds
    )

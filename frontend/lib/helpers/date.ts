import { DateTime } from 'luxon';

/**
 * Get a human-readable relative time string
 * @param date Represents the date to be converted
 * @param pivot Represents the date to compare with as a starting point
 *
 * @returns A string representing the human-readable relative time
 * (e.g. 'Just now', '2 minutes ago', '1 year ago')
 */
export function humanRelativeTime(
  date: DateTime | string,
  pivot?: DateTime | string | null
): string | null {
  if (typeof date === 'string') date = DateTime.fromISO(date);
  if (typeof pivot === 'string') pivot = DateTime.fromISO(pivot);

  // If pivot is not provided, use current time
  pivot ??= DateTime.utc();

  // If the difference is within the last 2 minutes, show 'Just now'
  const minuteDelta = date.diff(pivot, 'minutes').minutes;
  if (Math.abs(minuteDelta) < 2) return 'Just now';

  // If the difference is a longer ago, show relative time
  const relative = date.toRelative({
    base: pivot,
    style: 'narrow',
  });

  if (!relative) return null;
  return relative.replace('.', '').split(' ').slice(0, 2).join(' ');
}

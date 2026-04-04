/**
 * 日期格式化工具
 */

/**
 * 格式化日期为 YYYY-MM-DD
 */
export function formatDate(date) {
  if (!date) return '-';
  try {
    if (typeof date === 'string') {
      if (date.includes('T')) return date.substring(0, 10);
      return date.substring(0, 10);
    }
    if (date instanceof Date) {
      const y = date.getFullYear();
      const m = String(date.getMonth() + 1).padStart(2, '0');
      const d = String(date.getDate()).padStart(2, '0');
      return `${y}-${m}-${d}`;
    }
    if (date.time) return formatDate(new Date(date.time));
    return String(date).substring(0, 10);
  } catch {
    return '-';
  }
}

/**
 * 格式化日期为 YYYY-MM-DD HH:mm:ss
 */
export function formatDateTime(date) {
  if (!date) return '-';
  try {
    let d;
    if (typeof date === 'string') {
      if (date.includes('T')) {
        d = new Date(date);
      } else {
        return date.substring(0, 19);
      }
    } else if (date instanceof Date) {
      d = date;
    } else if (date.time) {
      d = new Date(date.time);
    } else {
      return '-';
    }
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const h = String(d.getHours()).padStart(2, '0');
    const min = String(d.getMinutes()).padStart(2, '0');
    const s = String(d.getSeconds()).padStart(2, '0');
    return `${y}-${m}-${day} ${h}:${min}:${s}`;
  } catch {
    return '-';
  }
}

/**
 * 格式化日期为相对时间（刚刚、几分钟前等）
 */
export function formatRelativeTime(date) {
  if (!date) return '-';
  try {
    let d;
    if (typeof date === 'string') {
      d = new Date(date.includes('T') ? date : date.replace(/\//g, '-'));
    } else if (date instanceof Date) {
      d = date;
    } else if (date.time) {
      d = new Date(date.time);
    } else {
      return '-';
    }
    const now = new Date();
    const diff = now - d;
    const seconds = Math.floor(diff / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);

    if (seconds < 60) return '刚刚';
    if (minutes < 60) return `${minutes}分钟前`;
    if (hours < 24) return `${hours}小时前`;
    if (days < 30) return `${days}天前`;
    if (days < 365) return `${Math.floor(days / 30)}个月前`;
    return `${Math.floor(days / 365)}年前`;
  } catch {
    return '-';
  }
}

/**
 * 格式化日期为中文格式（2024年01月15日）
 */
export function formatDateChinese(date) {
  if (!date) return '-';
  const d = formatDate(date);
  if (d === '-') return '-';
  const [y, m, day] = d.split('-');
  return `${y}年${m}月${day}日`;
}

/**
 * 计算两个日期间的天数
 */
export function daysBetween(start, end) {
  try {
    const s = new Date(start);
    const e = new Date(end);
    return Math.ceil((e - s) / (1000 * 60 * 60 * 24));
  } catch {
    return 0;
  }
}

/**
 * 获取今天日期（YYYY-MM-DD）
 */
export function getToday() {
  return formatDate(new Date());
}

/**
 * 获取N天后的日期
 */
export function getDateAfter(days) {
  const d = new Date();
  d.setDate(d.getDate() + days);
  return formatDate(d);
}

/**
 * 检查日期是否即将到期（30天内）
 */
export function isUpcoming(date) {
  if (!date) return false;
  return daysBetween(new Date(), date) >= 0 && daysBetween(new Date(), date) <= 30;
}

/**
 * 检查日期是否已过期
 */
export function isExpired(date) {
  if (!date) return false;
  return daysBetween(new Date(), date) < 0;
}

/**
 * 获取距离某日期的天数
 */
export function daysUntil(date) {
  if (!date) return 0;
  return daysBetween(new Date(), date);
}

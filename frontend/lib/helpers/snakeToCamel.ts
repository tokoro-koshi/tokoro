function toCamelCase(str: string): string {
  return str.replace(/_([a-z])/g, (_, letter) => letter.toUpperCase());
}

export function snakeToCamel<T>(obj: T): T {
  if (Array.isArray(obj)) {
    return obj.map((v) => snakeToCamel(v)) as unknown as T;
  } else if (obj !== null && typeof obj === 'object') {
    return Object.keys(obj as Record<string, unknown>).reduce((result, key) => {
      const camelKey = toCamelCase(key);
      (result as Record<string, unknown>)[camelKey] = snakeToCamel(
        (obj as Record<string, unknown>)[key]
      );
      return result;
    }, {} as T);
  }
  return obj;
}

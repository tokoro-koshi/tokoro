export interface About {
  id: string;
  title: string;
  subtitle: string;
  logo: string;
  description: string;
  vision: string;
  mission: string;
  values: string[];
  establishedDate: string; // ISO date string format
  socialMedia: Record<string, string>; // Key-value pairs for platform URLs
}

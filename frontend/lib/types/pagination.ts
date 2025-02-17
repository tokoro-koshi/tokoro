export interface Pagination<T> {
  payload: T[];
  links: Link[];
  meta: PageMetadata;
}

export interface Link {
  href: string;
  hreflang?: string;
  title?: string;
  type?: string;
  deprecation?: string;
  profile?: string;
  name?: string;
  templated?: boolean;
}

export interface PageMetadata {
  size: number; // int64
  totalElements: number; // int64
  totalPages: number; // int64
  number: number; // int64
}

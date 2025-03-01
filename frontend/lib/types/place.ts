export interface Place {
  id: string;
  name: string;
  description: string;
  location: Location;
  categoryId: string;
  tags: Tag[];
  pictures: string[]; // Assuming pictures are stored as URLs or paths
  rating: number; // double
}

export interface Location {
  address: string;
  city: string;
  country: string;
  coordinate: Coordinate;
}

export interface Coordinate {
  latitude: number; // double
  longitude: number; // double
}

export interface Tag {
  lang: string;
  name: string;
}

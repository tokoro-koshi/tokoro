export interface Place {
    id: string,
    name: string,
    description: string,
    location: {
        address: string,
        city: string,
        country: string,
        coordinate: { latitude: number, longitude: number }
    },
    categoryId: string,
    tags: { lang: string, name: string }[],
    pictures: [string],
    rating: number
}

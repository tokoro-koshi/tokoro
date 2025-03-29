import { Place } from '@/lib/types/place';

export const places: Place[] = [
    {
        id: '1',
        name: 'Galicyjska Kasa Oszczednosci',
        description:
            'A historical landmark showcasing the architectural beauty of the Galician era.',
        location: {
            address: 'Main Square 1',
            city: 'Krakow',
            country: 'Poland',
            coordinate: { latitude: 50.0614, longitude: 19.9366 },
        },
        categoryId: 'Historical Landmark',
        tags: [
            { lang: 'en', name: 'history' },
            { lang: 'pl', name: 'historia' },
        ],
        pictures: ['/place-carousel.svg'],
        rating: 4.9,
    },
    {
        id: '2',
        name: 'Cozy Café',
        description:
            'A charming café perfect for enjoying fresh brews and cozy conversations.',
        location: {
            address: 'Green Street 12',
            city: 'Warsaw',
            country: 'Poland',
            coordinate: { latitude: 52.2297, longitude: 21.0122 },
        },
        categoryId: 'Food & Drink',
        tags: [
            { lang: 'en', name: 'coffee' },
            { lang: 'pl', name: 'kawa' },
        ],
        pictures: ['/place-carousel.svg'],
        rating: 4.8,
    },
    {
        id: '3',
        name: 'Art Gallery',
        description:
            'Explore contemporary and classic artworks from renowned and emerging artists.',
        location: {
            address: 'Art Lane 45',
            city: 'Gdansk',
            country: 'Poland',
            coordinate: { latitude: 54.352, longitude: 18.6466 },
        },
        categoryId: 'Culture',
        tags: [
            { lang: 'en', name: 'art' },
            { lang: 'pl', name: 'sztuka' },
        ],
        pictures: ['/place-carousel.svg'],
        rating: 4.7,
    },
    {
        id: '4',
        name: 'Historic Landmark',
        description:
            'Discover a landmark rich in history and cultural significance.',
        location: {
            address: 'Heritage Road 8',
            city: 'Wroclaw',
            country: 'Poland',
            coordinate: { latitude: 51.1079, longitude: 17.0385 },
        },
        categoryId: 'Sightseeing',
        tags: [
            { lang: 'en', name: 'landmark' },
            { lang: 'pl', name: 'zabytek' },
        ],
        pictures: ['/place-carousel.svg'],
        rating: 4.9,
    },
    {
        id: '5',
        name: 'Botanical Garden',
        description:
            'A serene garden filled with a variety of plant species from around the world.',
        location: {
            address: 'Flora Street 2',
            city: 'Poznan',
            country: 'Poland',
            coordinate: { latitude: 52.4064, longitude: 16.9252 },
        },
        categoryId: 'Nature',
        tags: [
            { lang: 'en', name: 'garden' },
            { lang: 'pl', name: 'ogrod' },
        ],
        pictures: ['/place-carousel.svg'],
        rating: 4.6,
    },
];

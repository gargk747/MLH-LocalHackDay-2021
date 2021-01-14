const config = require( './package.json' );

const { title, description, author, repository, homepage } = config;

const siteMetadata = {
  companyName: title,
  companyUrl: repository.url,
  authorName: author.name,
  authorUrl: author.url,
  siteUrl: homepage,
  siteDescription: description,
};

module.exports = {
  siteMetadata,
  plugins: [
    'gatsby-plugin-resolve-src',
    {
      resolve: 'gatsby-plugin-sass',
      options: {
        implementation: require( 'sass' ),
      },
    },
    {
      resolve: 'gatsby-source-graphql',
      options: {
        typeName: 'GCMS',
        fieldName: 'gcms',
        url: 'https://api-eu-central-1.graphcms.com/v2/ckjx783nx5thu01wkeg5y01oa/master',
      }
    },
    'gatsby-plugin-react-helmet',
    {
      resolve: `gatsby-source-filesystem`,
      options: {
        name: `images`,
        path: `${__dirname}/src/assets/images`,
      },
    },
    'gatsby-plugin-react-leaflet',
    {
      resolve: 'gatsby-plugin-manifest',
      options: {
        name: siteMetadata.companyName,
        short_name: siteMetadata.companyName,
        start_url: '/',
        icon: 'src/assets/images/react-leaflet-icon.png',
      },
    },
  ],
};

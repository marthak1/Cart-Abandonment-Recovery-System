import ProductList from "../components/ProductList";
const Home = () => {
  return (
    <>
      <div className="container mx-auto px-4 py-6">
        <h1 className="text-3xl font-bold text-center mb-6">
          Welcome to Our Store 🛍️
        </h1>
      </div>
      <ProductList />
    </>
  );
};
export default Home;

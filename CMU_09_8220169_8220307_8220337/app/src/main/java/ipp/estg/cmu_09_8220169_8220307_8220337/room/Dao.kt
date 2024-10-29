package ipp.estg.cmu_09_8220169_8220307_8220337.room

import androidx.room.Dao

/**
 * suspend functions permitem executar sem bloquear o programa
 */
@Dao
interface Dao {

//    @Query("SELECT * FROM pokemonentity")
//    suspend fun getAllPokemons(): List<PokemonEntity>
//
//    @Query("Select * from pokemonentity where name LIKE :query")
//    suspend fun searchPokemon(query:String): List<PokemonEntity>
//
//    @Query("SELECT * FROM pokemonentity LIMIT :limit OFFSET :offset")
//    suspend fun getPokemonsWithLimitAndOffset(limit: Int, offset: Int): List<PokemonEntity>
//
//
//    //    @Insert(onConflict = OnConflictStrategy.REPLACE) // @Upsert faz o mesmo
//    @Upsert
//    suspend fun insert(pokemons: List<PokemonEntity>): List<Long>
//
//    @Delete
//    suspend fun delete(pokemons: List<PokemonEntity>): Int
//
//    @Query("DELETE FROM pokemonentity")
//    suspend fun deleteAll()

}